package org.frank.common.util.sign;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Base64工具类 - 优化版本
 * 集成密码加密功能，支持多种加密方式
 *
 * @author Frank
 */
public final class Base64Utils {

    /**
     * 使用Java 8内置Base64编码
     *
     * @param data 原始数据
     * @return Base64编码字符串
     */
    public static String encode(byte[] data) {
        if (data == null) {
            return null;
        }
        return Base64.getEncoder().encodeToString(data);
    }

    /**
     * 使用Java 8内置Base64编码字符串
     *
     * @param text 原始字符串
     * @return Base64编码字符串
     */
    public static String encode(String text) {
        if (text == null) {
            return null;
        }
        return encode(text.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 使用Java 8内置Base64解码
     *
     * @param encoded Base64编码字符串
     * @return 解码后的字节数组
     */
    public static byte[] decode(String encoded) {
        if (encoded == null) {
            return null;
        }
        return Base64.getDecoder().decode(encoded);
    }

    /**
     * 使用Java 8内置Base64解码字符串
     *
     * @param encoded Base64编码字符串
     * @return 解码后的字符串
     */
    public static String decodeToString(String encoded) {
        byte[] decoded = decode(encoded);
        if (decoded == null) {
            return null;
        }
        return new String(decoded, StandardCharsets.UTF_8);
    }

    /**
     * URL安全的Base64编码
     *
     * @param data 原始数据
     * @return URL安全的Base64编码字符串
     */
    public static String encodeUrlSafe(byte[] data) {
        if (data == null) {
            return null;
        }
        return Base64.getUrlEncoder().encodeToString(data);
    }

    /**
     * URL安全的Base64解码
     *
     * @param encoded URL安全的Base64编码字符串
     * @return 解码后的字节数组
     */
    public static byte[] decodeUrlSafe(String encoded) {
        if (encoded == null) {
            return null;
        }
        return Base64.getUrlDecoder().decode(encoded);
    }

    /**
     * 密码加密 - 使用MD5+Base64方式
     * 注意：这种方式不如BCrypt安全，仅用于兼容性
     *
     * @param password 原始密码
     * @return 加密后的密码
     */
    public static String encryptPassword(String password) {
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }

        // 先进行MD5哈希
        String md5Hash = Md5Utils.hash(password);
        // 再进行Base64编码
        return encode(md5Hash);
    }

    /**
     * 验证密码 - MD5+Base64方式
     *
     * @param rawPassword       原始密码
     * @param encryptedPassword 加密后的密码
     * @return 是否匹配
     */
    public static boolean verifyPassword(String rawPassword, String encryptedPassword) {
        if (rawPassword == null || encryptedPassword == null) {
            return false;
        }

        try {
            String encrypted = encryptPassword(rawPassword);
            return encrypted.equals(encryptedPassword);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 测试方法
     */
    public static void main(String[] args) {
        String password = "frank";

        System.out.println("=== Base64 编码测试 ===");
        String encoded = encode(password);
        String decoded = decodeToString(encoded);
        System.out.println("原始: " + password);
        System.out.println("编码: " + encoded);
        System.out.println("解码: " + decoded);

        System.out.println("\n=== 密码加密测试 ===");

        // MD5+Base64 方式
        String md5Encrypted = encryptPassword(password);
        System.out.println("MD5+Base64: " + md5Encrypted);
        System.out.println("MD5验证: " + verifyPassword(password, md5Encrypted));
    }
}