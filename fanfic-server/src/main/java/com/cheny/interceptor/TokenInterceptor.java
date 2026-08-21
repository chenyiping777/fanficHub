package com.cheny.interceptor;


import com.cheny.utils.CurrentHolder;
import com.cheny.utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Component
public class TokenInterceptor implements HandlerInterceptor {


    @Autowired
    private JwtUtil jwtUtil;


    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        String token = request.getHeader("token");
        // 令牌为空直接拒绝
        if (token == null || token.trim().isEmpty()) {
            response.setStatus(401);
            response.getWriter().write("未携带令牌，请先登录");
            return false;
        }
        // 校验令牌
        if (!jwtUtil.verifyToken(token)) {
            response.setStatus(401);
            response.getWriter().write("令牌失效或非法，请重新登录");
            return false;
        }
        Long userId = (Long) jwtUtil.getPayload(token).getClaim("userId");
        //获取载荷
        String role = (String) jwtUtil.getPayload(token).getClaim("role");


        CurrentHolder.setRole(role);
        CurrentHolder.setCurrentId(userId);
        log.info("当前用户 id：{}，角色：{}", userId, role);

        return true;
    }


    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {
        //删除 ThreadLocal 中的变量
        CurrentHolder.remove();
    }

}
