package com.cheny.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.cheny.constant.MessageConstant;
import com.cheny.constant.StatusConstant;
import com.cheny.dto.SetmealDto;
import com.cheny.entity.Dish;
import com.cheny.entity.Setmeal;
import com.cheny.entity.SetmealDish;
import com.cheny.exception.DishOperationException;
import com.cheny.exception.SetmealOperationException;
import com.cheny.query.SetmealQuery;
import com.cheny.service.DishService;
import com.cheny.service.SetmealDishService;
import com.cheny.service.SetmealService;
import com.cheny.mapper.SetmealMapper;
import com.cheny.utils.AliOSSUtils;
import com.cheny.vo.DishItemVo;
import com.cheny.vo.PageVo;
import com.cheny.vo.SetmealVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Mlpnk
 * @description 针对表【setmeal(明星组合/CP表)】的数据库操作Service实现
 * @createDate 2026-08-15 11:56:10
 */
@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal>
        implements SetmealService {


    @Autowired
    private AliOSSUtils aliOSSUtils;

    @Autowired
    private SetmealDishService setmealDishService;

    @Autowired
    private DishService dishService;

    @Override
    public void updateStatus(Integer status, Long id) {
        //首先根据id获取setMeal对象
        Setmeal setmeal = getById(id);
        //起售套餐时，判断套餐内是否有停售菜品，有停售菜品提示"套餐内包含未启售菜品，无法启售"
        //setmealDishes表示这个套餐里面包含的所有菜品
        List<SetmealDish> setmealDishes = setmealDishService.list(Wrappers.<SetmealDish>lambdaQuery().eq(SetmealDish::getSetmealId, id));
        if (CollUtil.isNotEmpty(setmealDishes)) {
            List<Long> ids = setmealDishes.stream().map(setmealDish -> setmealDish.getId()).toList();
            Long cnt = dishService.lambdaQuery()
                    .in(Dish::getId, ids)
                    .eq(Dish::getStatus, StatusConstant.DISABLE)
                    .count();
            if (cnt > 0) {
                throw new SetmealOperationException(MessageConstant.SETMEAL_ENABLE_FAILED);
            }
        }
        //再修改状态
        setmeal.setStatus(status);
        //再做更新
        save(setmeal);
    }

    @Override
    public PageVo<SetmealVo> pageSetmeal(SetmealQuery setMealQuery) {
        //构造分页参数
        Integer pageNo = setMealQuery.getPageNo();
        Integer pageSize = setMealQuery.getPageSize();
        Page<SetmealVo> pageParam = new Page<>(pageNo, pageSize);

        //构造查询对象--这里分页展示，不需要关注套餐有哪些菜品
        LambdaQueryWrapper<Setmeal> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(setMealQuery.getCategoryId() != null, Setmeal::getCategoryId, setMealQuery.getCategoryId())
                .eq(setMealQuery.getName() != null, Setmeal::getName, setMealQuery.getName())
                .eq(setMealQuery.getStatus() != null, Setmeal::getStatus, setMealQuery.getStatus());


        //调用mapper开始分页

        Page<SetmealVo> page = baseMapper.selectPageSetmeal(pageParam, lambdaQueryWrapper);

        //结果转换Vo

        PageVo<SetmealVo> pageVo = new PageVo<>();

        pageVo.setTotal(page.getTotal());//总条数
        pageVo.setPages(page.getPages());//总页数
        pageVo.setList(page.getRecords());//当前页数据
        return pageVo;
    }

    @Override
    @Transactional
    public void addSetmeal(SetmealDto setmealDto, MultipartFile imageFile) {
        //套餐的名字不能重复
        Long cnt = lambdaQuery().eq(Setmeal::getName, setmealDto.getName()).count();
        if (cnt > 0) {
            throw new DishOperationException(MessageConstant.PARAM_ILLEGAL);
        }
        // ============OSS图片处理核心逻辑============
        // 如果上传了新图片文件，调用oss上传，拿到新url，覆盖setmealDto.image
        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                String newImageUrl = aliOSSUtils.upload(imageFile);
                setmealDto.setImage(newImageUrl);
            } catch (Exception e) {
                //捕获oss上传IO异常，包装为业务异常抛出
                log.error(MessageConstant.UPLOAD_FAILED, e);
                throw new SetmealOperationException(MessageConstant.UPLOAD_FAILED);
            }
        }
        // 如果imageFile为null，代表用户没有上传新图片，setmealDto.image里面保存的就是老的oss地址，直接往下复制属性即可
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDto, setmeal);
        //先插主表
        save(setmeal);//MyBatis-Plus 的 save() 会把自增主键回填到 setmeal 对象


        Long setmealId = setmeal.getId();
        // 2. 再插子表:给每条口味表补上 dish_id,然后批量插入

        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        //口味选择如果为空就不添加
        if (CollUtil.isNotEmpty(setmealDishes)) {
            for (SetmealDish e : setmealDishes) {
                e.setSetmealId(setmealId);// 关联主表主键
            }
            setmealDishService.saveBatch(setmealDishes);
        }
    }

    @Override
    @Transactional
    public void updateSetmeal(SetmealDto setmealDto, MultipartFile imageFile) {
        //套餐的名字不能重复
        Long cnt = lambdaQuery().eq(Setmeal::getName, setmealDto.getName())
                .ne(Setmeal::getId, setmealDto.getId())
                .count();
        if (cnt > 0) {
            throw new DishOperationException(MessageConstant.PARAM_ILLEGAL);
        }
        // ============OSS图片处理核心逻辑============
        // 如果上传了新图片文件，调用oss上传，拿到新url，覆盖setmealDto.image
        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                String newImageUrl = aliOSSUtils.upload(imageFile);
                setmealDto.setImage(newImageUrl);
            } catch (Exception e) {
                //捕获oss上传IO异常，包装为业务异常抛出
                log.error(MessageConstant.UPLOAD_FAILED, e);
                throw new SetmealOperationException(MessageConstant.UPLOAD_FAILED);
            }
        }
        // 如果imageFile为null，代表用户没有上传新图片，setmealDto.image里面保存的就是老的oss地址，直接往下复制属性即可
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDto, setmeal);
        //先插主表
        save(setmeal);//MyBatis-Plus 的 save() 会把自增主键回填到 setmeal 对象


        Long setmealId = setmeal.getId();
        // 2. 再插子表:给每条口味表补上 dish_id,然后批量插入
        //先把子表相关记录都删了
        setmealDishService.remove(Wrappers.<SetmealDish>lambdaQuery().eq(SetmealDish::getSetmealId, setmealId));
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        //口味选择如果为空就不添加
        if (CollUtil.isNotEmpty(setmealDishes)) {
            for (SetmealDish e : setmealDishes) {
                e.setSetmealId(setmealId);// 关联主表主键
            }
            setmealDishService.saveBatch(setmealDishes);
        }
    }

    @Override
    public List<DishItemVo> getDishItemVosById(Long id) {
        //三表联合
        return baseMapper.getDishItemBySetmealId(id);
    }

    @Override
    public void removeSetmealById(Long id) {
        //起售中的套餐不能删除
        Setmeal setmeal = getById(id);
        if (setmeal.getStatus() == StatusConstant.ENABLE) {
            throw new SetmealOperationException(MessageConstant.SETMEAL_ON_SALE);
        }
        removeById(id);
    }

    @Override
    public void removeSetmealByIds(List<Long> ids) {
        Long cnt = lambdaQuery().eq(Setmeal::getStatus, StatusConstant.DISABLE).in(Setmeal::getId, ids).count();
        if (cnt > 0) {
            throw new SetmealOperationException(MessageConstant.SETMEAL_ON_SALE);
        }
        removeByIds(ids);
    }

    @Override
    public List<SetmealVo> getSetmealByCategoryId(Long categoryId) {
        List<Setmeal> setmeals = lambdaQuery().eq(Setmeal::getCategoryId, categoryId)
                                                .eq(Setmeal::getStatus, StatusConstant.ENABLE)
                                                .list();
        return setmeals.stream().map(setmeal -> {
                            SetmealVo setmealVo = new SetmealVo();
                            BeanUtils.copyProperties(setmeal, setmealVo);
                            return setmealVo;
        }).collect(Collectors.toList());
    }
}




