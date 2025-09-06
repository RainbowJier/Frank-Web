package org.frank.common.util.sign;

import at.favre.lib.crypto.bcrypt.BCrypt;

/**
 * BCrypt密码工具类
 * 
 * 专门用于密码的哈希和验证，比AES更安全
 * 
 * @author Frank
 */
public class BCryptUtils {
    
    /**
     * 密码哈希 - 使用默认强度(10)
     * 
     * @param rawPassword 原始密码
     * @return 哈希后的密码
     */
    public static String hashPassword(String rawPassword) {
        if (rawPassword == null || rawPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        return BCrypt.withDefaults().hashToString(12, rawPassword.toCharArray());
    }
    
    /**
     * 验证密码
     * 
     * @param rawPassword 原始密码
     * @param hashedPassword 哈希后的密码
     * @return 是否匹配
     */
    public static boolean matchesPassword(String rawPassword, String hashedPassword) {
        if (rawPassword == null || hashedPassword == null) {
            return false;
        }
        
        try {
            BCrypt.Result result = BCrypt.verifyer().verify(rawPassword.toCharArray(), hashedPassword);
            return result.verified;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * 检查密码是否需要重新哈希（例如需要升级强度）
     * 
     * @param hashedPassword 哈希后的密码
     * @return 是否需要重新哈希
     */
    public static boolean needsRehash(String hashedPassword) {
        if (hashedPassword == null || hashedPassword.isEmpty()) {
            return true;
        }
        
        try {
            // 解析哈希密码检查成本因子
            if (hashedPassword.startsWith("$2a$") || hashedPassword.startsWith("$2b$")) {
                String[] parts = hashedPassword.split("\\$");
                if (parts.length >= 4) {
                    int cost = Integer.parseInt(parts[2]);
                    // 如果成本因子小于12，建议重新哈希
                    return cost < 12;
                }
            }
            return true;
        } catch (Exception e) {
            return true;
        }
    }
    
    /**
     * 测试
     */
    public static void main(String[] args) {
        String original = "frank";
        
        // 哈希密码
        String hashed1 = hashPassword(original);
        
        System.out.println("Original: " + original);
        System.out.println("Hashed1: " + hashed1);
        
        // 验证密码
        boolean valid1 = matchesPassword(original, hashed1);
        
        System.out.println("Valid1: " + valid1);
    }
}