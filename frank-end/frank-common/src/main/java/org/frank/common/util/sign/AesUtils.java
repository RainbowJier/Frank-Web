package org.frank.common.util.sign;

import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.symmetric.AES;

import java.nio.charset.StandardCharsets;

public class AesUtils {
    private static final String SECRET = "1234567890123456";
    private static final String IV = "1234567890123456";

    private static final AES aes = new AES(Mode.ECB, Padding.PKCS5Padding, SECRET.getBytes(StandardCharsets.UTF_8));

    /**
     * 加密 - 使用ECB模式确保相同输入产生相同输出
     *
     * @param value 需要加密的字符串
     * @return 加密后的Base64编码字符串
     */
    public static String encrypt(String value) {
        return aes.encryptBase64(value, StandardCharsets.UTF_8);
    }

    /**
     * 解密
     *
     * @param encrypted Base64编码的加密字符串
     * @return 解密后的原始字符串
     */
    public static String decrypt(String encrypted) {
        return aes.decryptStr(encrypted, StandardCharsets.UTF_8);
    }

    /**
     * 验证密码
     *
     * @param rawPassword 原始密码
     * @param encryptedPassword 加密后的密码
     * @return 是否匹配
     */
    public static boolean matchesPassword(String rawPassword, String encryptedPassword) {
        try {
            String encrypted = encrypt(rawPassword);
            return encrypted.equals(encryptedPassword);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 测试
     */
    public static void main(String[] args) {
        String original = "admin";
        String encrypted1 = encrypt(original);
        String encrypted2 = encrypt(original);
        String decrypted = decrypt(encrypted1);

        System.out.println("Original: " + original);
        System.out.println("Encrypted1: " + encrypted1);
        System.out.println("Encrypted2: " + encrypted2);
        System.out.println("Same result: " + encrypted1.equals(encrypted2));
        System.out.println("Decrypted: " + decrypted);
        System.out.println("Verification successful: " + original.equals(decrypted));
    }
}