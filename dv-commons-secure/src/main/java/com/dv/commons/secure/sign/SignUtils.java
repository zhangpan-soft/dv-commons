package com.dv.commons.secure.sign;

import com.dv.commons.secure.SecureUtil;
import com.dv.commons.secure.Sign;
import org.apache.commons.codec.binary.Hex;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;


/**
 * @author zhangpan
 * @description
 * @date 2021/6/10/010 18:39
 */
public class SignUtils {
    public static final String KEY = "key";
    public static final String NONCE_STR = "nonceStr";
    public static final String SIGN_TYPE = "signType";
    public static final String SIGN = "sign";

    private static final String EQUAL = "=";
    private static final String SEPARATOR = "&";
    private static final String ALGORITHM_HMACSHA256 = "HmacSHA256";

    /**
     * 签名
     * @param map 请求参数
     * @param key 秘钥
     * @param signType 签名方式
     * @return 签名字符串
     */
    public static String sign(Map<String,String> map,String key,SignType signType) {
        return sign(map, key, signType,null);
    }

    private static String waitSignPlainText(Map<String,String> map,String key,SignType signType,String nonceStr){
        // 添加key
        if (signType!=SignType.RSA2){
            map.put(KEY,key);
        }
        if (nonceStr==null||nonceStr.isBlank()){
            // 添加随机字符串
            map.put(NONCE_STR, UUID.randomUUID().toString());
        }
        // 指定签名方式
        map.put(SIGN_TYPE,signType.name());
        // ASCII排序
        Map<String,String> treeMap = new TreeMap<>(map);
        StringBuilder sb = new StringBuilder();
        treeMap.forEach((k,v)->{
            // 如果值不为空,并且键不是sign则拼接待加密字符串
            if (v!=null&&!v.isBlank()&&!SIGN.equalsIgnoreCase(k)){
                sb.append(k).append(EQUAL).append(v).append(SEPARATOR);
            }
        });
        if (sb.length()>0){
            // 删除最后一个&
            sb.deleteCharAt(sb.length()-1);
        }
        return sb.toString();
    }

    private static String sign(Map<String,String> map,String key,SignType signType,String nonceStr){
        String sign = null;
        // 如果是md5加签
        String plainText = waitSignPlainText(map, key, signType, nonceStr);
        if (SignType.MD5==signType){
            sign = SecureUtil.md5Hex(plainText);
        }else if (SignType.HMACSHA256==signType){
            sign = hmacsha256(plainText, key).toUpperCase();
        }else if (SignType.RSA2==signType){
            sign = SecureUtil.rsa().base64Key(key).plaintext(plainText).sign().base64Strings();
        }
        map.remove(KEY);
        return sign;
    }

    /**
     *
     * 签名
     * @param map 请求参数
     * @param key 秘钥
     * @param signType 签名方式
     * @return 签名之后的map,含有sign
     */
    public static Map<String,String> signMap(Map<String,String> map,String key,SignType signType) {
        map.put(SIGN,sign(map,key,signType,null));
        return map;
    }

    /**
     * 签名验证
     * @param data 请求参数
     * @param key 秘钥
     * @return {@link Boolean}
     */
    public static boolean isValid(Map<String,String> data,String key)  {
        if (!data.containsKey(SIGN)||!data.containsKey(SIGN_TYPE)) {
            return false;
        } else if (!data.get(SIGN_TYPE).equalsIgnoreCase(SignType.RSA2.name())){
            String sign = data.get(SIGN);
            return sign(data, key, SignType.valueOf(data.get(SIGN_TYPE)),data.get(NONCE_STR)).equals(sign);
        } else {
            return SecureUtil.rsa().base64Key(key).plaintext(waitSignPlainText(data,key,SignType.RSA2,data.get(NONCE_STR))).verify(new Sign.SignImpl(Base64.getDecoder().decode(data.get(SIGN))));
        }
    }

    /**
     * hmacsha256签名
     * @param data 待签名字符串
     * @param key 秘钥
     * @return {@link String}
     */
    public static String hmacsha256(String data, String key)  {
        try {
            Mac sha256HMAC = Mac.getInstance(ALGORITHM_HMACSHA256);
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), ALGORITHM_HMACSHA256);
            sha256HMAC.init(secretKey);
            byte[] array = sha256HMAC.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return Hex.encodeHexString(array).toUpperCase();
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
