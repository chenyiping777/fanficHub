package com.cheny.utils;

// Jackson核心对象，用于java对象和JSON字符串互转
import com.fasterxml.jackson.databind.ObjectMapper;
// Jackson的JSON对象节点，用来构造json对象
import com.fasterxml.jackson.databind.node.ObjectNode;
// Bean初始化回调注解，Spring完成属性注入后自动调用
import jakarta.annotation.PostConstruct;
// Lombok日志注解，自动生成 log 对象，替代 System.out/err
import lombok.extern.slf4j.Slf4j;
// 从配置文件读取属性值
import org.springframework.beans.factory.annotation.Value;
// 标记为Spring组件（Bean），交给Spring容器管理
import org.springframework.stereotype.Component;

import java.io.IOException;
// URI 统一资源标识符，http请求的地址对象
import java.net.URI;
// URL编码工具，处理get参数、表单参数中文特殊字符转义
import java.net.URLEncoder;
// java11新增http客户端核心类
import java.net.http.HttpClient;
// http请求构建器
import java.net.http.HttpRequest;
// http响应对象
import java.net.http.HttpResponse;
// 字符编码
import java.nio.charset.StandardCharsets;
// 时间时长类，设置超时时间
import java.time.Duration;
// Map存请求参数
import java.util.Map;
// 字符串拼接工具，用来拼接 & 分隔的参数串
import java.util.StringJoiner;

/**
 * HTTP 请求工具类 —— 基于 Java 11+ 内置 java.net.http.HttpClient
 *
 * <p>提供三种常用请求方式：
 * <ul>
 *   <li>{@link #doGet(String, Map)}            — GET 请求，参数自动拼接到 URL</li>
 *   <li>{@link #doPost(String, Map)}           — POST 表单提交（application/x-www-form-urlencoded）</li>
 *   <li>{@link #doPost4Json(String, Map)}      — POST JSON 提交（application/json）</li>
 * </ul>
 *
 * <p>Spring Bean 方式管理：
 * <ul>
 *   <li>通过 {@code @Autowired} 注入使用，不要手动 new</li>
 *   <li>ObjectMapper 复用 Spring 容器里已有的全局实例，不需要自己创建</li>
 *   <li>超时时间可在 application.yml 里通过 {@code http.client.timeout-seconds} 配置</li>
 * </ul>
 */
@Component
@Slf4j
public class HttpClientUtil {


    private final ObjectMapper objectMapper;


    @Value("${http.client.timeout-seconds:5}")
    private int timeoutSeconds;

    /**
     * 根据超时配置计算出的 Duration 对象，在 @PostConstruct 中初始化。
     * <p>不能在声明时直接写 {@code Duration.ofSeconds(timeoutSeconds)}，
     * 因为 @Value 还没注入，timeoutSeconds 还是 0。
     */
    private Duration timeout;


    private HttpClient httpClient;

    /**
     * 构造器注入 ObjectMapper
     */
    public HttpClientUtil(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }


    @PostConstruct// 初始化方法，在依赖注入完成后自动调用，用于执行一些初始化操作
    private void init() {
        this.timeout = Duration.ofSeconds(timeoutSeconds);
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(timeout)          // TCP建立连接的超时时间，连不上直接失败
                .followRedirects(HttpClient.Redirect.NORMAL) // 自动跟随重定向
                .build();//整个 HTTP 请求的总超时
/*
*   服务器挂了，TCP 握手 6 秒才完成不了 → connectTimeout 触发，5 秒报错。
*   TCP 握手 1 秒就连上了，但是服务端业务逻辑卡住，要 6 秒才返回数据。握手成功了
* ，connectTimeout 不会管，HttpRequest.timeout 触发，总耗时 5 秒直接超时报错。
*
*/

        log.info("HttpClientUtil 初始化完成，超时时间：{}s", timeoutSeconds);
    }

    // ========================= GET 请求 =========================

    /**
     * 发送 GET 请求
     *
     * @param url      请求地址，例如 "https://api.example.com/users"
     * @param paramMap 查询参数（可为 null），会自动 URL 编码拼接到 URL 后面
     * @return 响应体字符串；请求失败时返回空字符串 ""
     *
     * <p>使用示例（在 Service 里注入后使用）：
     * <pre>{@code
     *   @Autowired
     *   private HttpClientUtil httpClientUtil;
     *
     *   Map<String, String> params = Map.of("page", "1", "size", "10");
     *   String result = httpClientUtil.doGet("https://api.example.com/users", params);
     * }</pre>
     */
    public String doGet(String url, Map<String, String> paramMap) {
        try {
            // 1. 构建带查询参数的完整 URI，把map参数拼接在url?后面
            URI uri = buildUri(url, paramMap);

            // 2. 构造 GET 请求
            HttpRequest request = HttpRequest.newBuilder() // 请求构建器，链式调用
                    .uri(uri) // 设置请求地址uri对象
                    .timeout(timeout)       // 单次完整请求读取超时，超时没返回数据直接抛异常
                    .GET() // 设置请求方法为GET
                    .build(); // 构建出不可变HttpRequest对象

            // 3. 发送请求并获取响应体（以字符串形式）
            // send：同步阻塞发送；BodyHandlers.ofString：把响应体转为UTF‑8字符串
            HttpResponse<String> response = httpClient.send(request,
                    HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

            // 4. 只返回 200 状态的响应，其他状态码(404/500/401)记录警告并返回空串
            if (response.statusCode() == 200) {
                return response.body();// 返回响应体字符串
            }
            // 非200状态码，用 @Slf4j 提供的 log 记录警告
            log.warn("GET {} 返回非200状态码: {}", url, response.statusCode());

        } catch (IOException e) {
            // IO异常：网络不通、连接超时、DNS解析失败、断开连接等网络层面异常
            log.error("GET 请求失败: {} -> {}", url, e.getMessage());
        } catch (InterruptedException e) {
            // 线程中断异常：调用线程被其他地方调用interrupt()打断
            Thread.currentThread().interrupt(); // 恢复线程中断标记，上层可以感知，不要吞掉中断状态
            log.error("GET 请求被中断: {}", url);
        }

        // 异常、非200状态码，统一返回空字符串
        return "";
    }

    // ========================= POST 表单请求 =========================

    /**
     * 发送 POST 请求 —— 表单格式（application/x-www-form-urlencoded）
     * 就是普通网页form表单提交，key1=val1&key2=val2格式body
     *
     * @param url      请求地址
     * @param paramMap 表单参数（可为 null）
     * @return 响应体字符串；请求失败时抛出 IOException
     * @throws IOException 网络异常时抛出，由调用方决定如何处理
     *
     * <p>使用示例：
     * <pre>{@code
     *   Map<String, String> form = Map.of("username", "admin", "password", "123456");
     *   String result = httpClientUtil.doPost("https://api.example.com/login", form);
     * }</pre>
     */
    public String doPost(String url, Map<String, String> paramMap) throws IOException {
        // 1. 将参数 Map 编码为 "key1=value1&key2=value2" 表单字符串
        String formBody = buildFormBody(paramMap);

        // 2. 构造 POST 请求
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url)) // 直接把字符串url转URI对象
                .timeout(timeout) // 请求总超时
                .header("Content-Type", "application/x-www-form-urlencoded") // 请求头声明body是表单格式
                // POST请求体发布器，传入表单字符串
                .POST(HttpRequest.BodyPublishers.ofString(formBody))
                .build();

        // 3. 发送请求
        try {
            HttpResponse<String> response = httpClient.send(request,
                    HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            return response.body(); // 直接返回原始响应字符串，不管http状态码
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // 恢复中断标记
            // 包装成IOException往上抛，调用者捕获处理
            throw new IOException("POST 请求被中断: " + url, e);
        }
    }

    // ========================= POST JSON 请求 =========================

    /**
     * 发送 POST 请求 —— JSON 格式（application/json）
     * body是json字符串，后端接口常用
     *
     * @param url      请求地址
     * @param paramMap 键值对参数（可为 null），会被序列化为 JSON 对象
     * @return 响应体字符串；请求失败时抛出 IOException
     * @throws IOException 网络异常或 JSON 序列化失败时抛出
     *
     * <p>使用示例：
     * <pre>{@code
     *   Map<String, String> data = Map.of("name", "张三", "age", "25");
     *   String result = httpClientUtil.doPost4Json("https://api.example.com/user", data);
     *   // 请求体: {"name":"张三","age":"25"}
     * }</pre>
     */
    public String doPost4Json(String url, Map<String, String> paramMap) throws IOException {
        // 1. 将 Map 序列化为 JSON 字符串（用 Spring 注入的 ObjectMapper）
        String jsonBody = toJsonString(paramMap);

        // 2. 构造 POST 请求，Content‑Type 设为 application/json
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(timeout)
                .header("Content-Type", "application/json; charset=utf-8") // 声明请求体是json
                // 把json字符串作为POST body，指定UTF‑8编码
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody, StandardCharsets.UTF_8))
                .build();

        // 3. 发送请求
        try {
            HttpResponse<String> response = httpClient.send(request,
                    HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            return response.body();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("POST JSON 请求被中断: " + url, e);
        }
    }

    // ========================= 内部工具方法 =========================

    /**
     * 将参数 Map 拼接到 URL 后面，生成完整的 URI
     * <p>参数值会做 URL 编码，防止中文或特殊字符导致请求失败
     *
     * @param url      基础 URL
     * @param paramMap 查询参数（可为 null）
     * @return 拼接后的 URI
     */
    private URI buildUri(String url, Map<String, String> paramMap) {
        // 参数为空，直接返回原始url转URI
        if (paramMap == null || paramMap.isEmpty()) {
            return URI.create(url);
        }

        // StringJoiner：用&作为分隔符拼接key=value片段
        StringJoiner joiner = new StringJoiner("&");
        // 遍历map每一个键值对
        for (Map.Entry<String, String> entry : paramMap.entrySet()) {
            // key做url编码，处理中文、空格、& =等特殊符号
            String key = URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8);
            // value同样url编码
            String value = URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8);
            joiner.add(key + "=" + value);
        }

        // 判断原url是否已经带有?，避免出现??或者&&错误
        // 如果url已经有?，参数用&拼接；没有就用?开启查询串
        String separator = url.contains("?") ? "&" : "?";
        // 拼接完整url字符串，转为URI对象返回
        return URI.create(url + separator + joiner);
    }

    /**
     * 将参数 Map 编码为表单格式字符串 "key1=value1&key2=value2"
     *
     * @param paramMap 表单参数（可为 null）
     * @return 编码后的表单字符串
     */
    private String buildFormBody(Map<String, String> paramMap) {
        // 参数为空返回空字符串
        if (paramMap == null || paramMap.isEmpty()) {
            return "";
        }

        StringJoiner joiner = new StringJoiner("&");
        for (Map.Entry<String, String> entry : paramMap.entrySet()) {
            // key、value都执行url编码，表单必须编码
            String key = URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8);
            String value = URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8);
            joiner.add(key + "=" + value);
        }
        return joiner.toString();
    }

    /**
     * 将 Map<String, String> 序列化为 JSON 字符串
     * <p>使用 Spring 注入的 ObjectMapper（已配好时区、日期格式等），替代过时的 fastjson
     *
     * <p>JSON：是文本字符串格式，一种数据格式，纯字符串，网络传输用。
     * <br>示例 JSON 字符串：{"name":"张三","age":"20"}
     *
     * <p>Jackson：是 Java 的 JSON 处理第三方库。
     * <br>ObjectMapper 就是 Jackson 库里面最核心的工具类。
     * <br>两个核心动作：
     * <br>序列化（写）：Java 对象 → JSON 字符串（往外发，传给对方）
     * <br>反序列化（读）：JSON 字符串 → Java 对象（对方返回，接收回来）
     *
     * @param paramMap 键值对参数（可为 null）
     * @return JSON 字符串，例如 {"name":"张三","age":"25"}
     * @throws IOException Jackson 序列化异常
     */
    private String toJsonString(Map<String, String> paramMap) throws IOException {
        // map为空返回{}，不要返回空字符串，避免后端解析json报错
        if (paramMap == null || paramMap.isEmpty()) {
            return "{}";
        }

        // 创建一个空JSON对象节点
        ObjectNode node = objectMapper.createObjectNode();
        // 循环map，把键值put进json对象
        for (Map.Entry<String, String> entry : paramMap.entrySet()) {
            node.put(entry.getKey(), entry.getValue());
        }
        // ObjectNode序列化为json字符串返回
        return objectMapper.writeValueAsString(node);
    }
}
