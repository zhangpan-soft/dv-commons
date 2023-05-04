# dv-commons
- dv-commons-jwt
- dv-commons-http
- dv-commons-base
- dv-commons-secure

# used
- 添加mirror或repository为: `https://nexus.51000.net/repository/maven-public/`

### dv-commons-jwt
```java
    @Test
    public void test() throws IOException {
        String key = UUID.randomUUID().toString().toUpperCase();
        Date expireAt = new Date(System.currentTimeMillis() + 10 * 1000);
        Map<String,Object> payload = new HashMap<>();
        payload.put("sub","123123123");
        payload.put("test","test");
        // 加签,并获取最终对象
        Operator operator = JwtUtil.builder().secret(key).audience("admin").issure("admin").expireAt(expireAt).generator(payload);
        // 打印Token
        System.out.println(operator.getToken());
        System.out.println(operator);
        // 验签,并获取对象
        Operator operator1 = JwtUtil.builder().secret(key).audience("admin").issure("admin").token(operator.getToken()).verify();
        System.out.println(operator1);
    }
```

### dv-commons-http
- 支持json,普通表单,multipart,等
- 支持PUT|POST|GET|DELETE|PATCH|HEAD
- 使用方式
```java
        System.out.println(HttpApiRequest.builder().get("https://www.baidu.com").data().build().doRequest());
        System.out.println(HttpApiRequest.builder().get("https://www.baidu.com").data("test","test").build().doRequest());
        System.out.println(HttpApiRequest.builder().get("https://www.baidu.com").data(new HashMap<>()).autoRedirect().global302Callback((request, response, location) -> {

        }).global401Callback((request, response) -> {

        }).cookie(new HashMap<>())
                .header("s","s")
                .referer("s").userAgent("s")
                .requestConfig(HttpRequestConfig.builder().proxy(HttpRequestConfig.HttpProxy.builder().host("").port(80).build()).build()).build()
                .doRequest());
        System.out.println(HttpApiRequest.builder().post("").json(new HashMap<>()).build().doRequest());
```

### dv-commons-secure
- 支持脚本类型为 `GlueTypeEnum` 中表述的
- bean或者groovy模式, 需要继承 `IHandler` 接口, bean模式
- 基础执行器为`BaseExecutor`继承`IExecutor`,如果需要自己重写基础执行器, 使用java原生spi机制即可
- 使用
```java
// 脚本
ExecutorUtil.Instance.execute(new ExecutorParam().setExecutorParams("").setGlueType(GlueTypeEnum.GLUE_PYTHON).setGlueSource("print(123)"));
// bean模式, 注意bean模式只支持spring bean, handleName为spring bean注入的名称
ExecutorUtil.Instance.execute(new ExecutorParam().setExecutorParams("").setGlueType(GlueTypeEnum.BEAN).setHandleName("myHandler"));
```