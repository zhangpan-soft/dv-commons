### 这是一个dsl语义的jwt工具包

### 使用方式:
```java
// 秘钥
String key = "<Your Key>";
// 过期时间
Date expireAt = new Date(System.currentTimeMillis() + 10 * 1000);
// 数据载荷
Map<String,Object> payload = new HashMap<>();
payload.put("sub","123123123");
payload.put("test","test");
// 加签,并获取最终对象
Operator operator = JwtUtil.builder().secret(key).audience("admin").issure("admin").expireAt(expireAt).generator(payload).build();
// 打印Token
System.out.println(operator.getToken());
System.out.println(operator);
// 验签,并获取对象
Operator operator1 = JwtUtil.builder().secret(key).audience("admin").issure("admin").token(operator.getToken()).verify().build();
System.out.println(operator1);
```
