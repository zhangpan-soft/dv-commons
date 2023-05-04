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
- 支持常见加密方式,
- 支持RSA2|MD5|HMACSHA256签名
- 使用
```java
 public void testGeneratorCode() throws NoSuchAlgorithmException {
        KeyGenerator instance = KeyGenerator.getInstance("HmacSHA1");
        instance.init(256, new SecureRandom());
        SecretKey key = instance.generateKey();
        System.out.println(Base64.getEncoder().encodeToString(key.getEncoded()));
        System.out.println(SecureTimeUtil.generatorCode(System.currentTimeMillis() / 1000, 30, key, "HmacSHA1"));

        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getEncoded(), "HmacSHA1");
        System.out.println(SecureTimeUtil.generatorCode(System.currentTimeMillis() / 1000, 30, secretKeySpec, "HmacSHA1"));

    }

    public void testGenerate() {

        RsaGenerator generator = SecureUtil.rsa().generator(2048);
        {
            RsaKeyGenerator<String> rsaKeyGenerator = generator.base64();
            System.out.println(rsaKeyGenerator.publicKey());
            System.out.println(rsaKeyGenerator.privateKey());
        }

        {
            RsaKeyGenerator<String> xml = generator.xml();
            System.out.println(xml.publicKey());
            System.out.println(xml.privateKey());
        }

        {
            System.out.println("-------------------");
            RsaKeyGenerator<byte[]> bytes = generator.bytes();
            System.out.println(new String(bytes.publicKey(), StandardCharsets.UTF_8));
            System.out.println("++++++++++++++++++++");
            System.out.println(new String(bytes.privateKey(), StandardCharsets.UTF_8));
            System.out.println("--------------------");
        }

        String strings = SecureUtil.rsa().base64Key(generator.base64().publicKey()).plaintext("这是个测试").base64Strings();
        System.out.println(strings);
        String strings1 = SecureUtil.rsa().base64Key(generator.base64().privateKey()).base64Ciphertext(strings).strings();
        System.out.println(strings1);

    }

    public void testSecret(){
        {
            RsaGenerator generator = SecureUtil.rsa().generator(2048);
            RsaKeyGenerator<String> keyGenerator = generator.base64();
            String publicKey = keyGenerator.publicKey();
            String privateKey = keyGenerator.privateKey();

            System.out.printf("公钥:%s\n",publicKey);
            System.out.printf("私钥:%s\n",privateKey);

            String ciphertext = SecureUtil.rsa().base64Key(publicKey).plaintext("这是个测试").base64Strings();
            System.out.printf("密文:%s\n", ciphertext);
            String plaintext = SecureUtil.rsa().base64Key(privateKey).base64Ciphertext(ciphertext).strings();
            System.out.printf("明文:%s\n", plaintext);
        }

        {
            AesKeyGenerator generator = SecureUtil.aes().generator(256);
            String s = generator.base64();
            System.out.printf("秘钥:%s\n",s);
            String ciphertext = SecureUtil.aes().base64Key(s).plaintext("这是个测试").base64Strings();
            System.out.printf("密文:%s\n", ciphertext);
            String plaintext = SecureUtil.aes().base64Key(s).base64Ciphertext(ciphertext).strings();
            System.out.printf("明文:%s\n", plaintext);
        }

    }

    public void testSign(){
        {
            String key = UUID.randomUUID().toString();
            Map<String, String> signMap = SignUtils.signMap(new HashMap<String, String>() {{
                this.put("test", "123");
            }}, key, SignType.HMACSHA256);
            System.out.println(signMap);
            boolean valid = SignUtils.isValid(signMap, key);
            System.out.println(valid);
        }

        {
            String key = UUID.randomUUID().toString();
            Map<String, String> signMap = SignUtils.signMap(new HashMap<String, String>() {{
                this.put("test", "123");
            }}, key, SignType.MD5);
            System.out.println(signMap);
            boolean valid = SignUtils.isValid(signMap, key);
            System.out.println(valid);
        }

        {
            RsaKeyGenerator<String> generator = SecureUtil.rsa().generator(2048).base64();
            Map<String, String> signMap = SignUtils.signMap(new HashMap<String, String>() {{
                this.put("test", "123");
            }}, generator.privateKey(), SignType.RSA2);
            System.out.println(signMap);
            boolean valid = SignUtils.isValid(signMap, generator.publicKey());
            System.out.println(valid);
        }

    }
```

### dv-commons-script
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