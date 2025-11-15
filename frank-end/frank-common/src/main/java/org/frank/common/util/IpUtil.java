package org.frank.common.util;

import cn.hutool.core.net.NetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.regex.Pattern;

/**
 * IP address utility class
 * Enhanced with Hutool toolkit and IP geolocation features
 */
@Slf4j
public class IpUtil {

    // IP geolocation API providers
    private static final String IP_API_URL1 = "https://ip-api.com/json/";
    private static final String IP_API_URL3 = "https://whois.pconline.com.cn/ipJson.jsp?ip=";
    // IP address validation patterns
    private static final Pattern IPV4_PATTERN = Pattern.compile(
            "^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$");
    private static final Pattern IPV6_PATTERN = Pattern.compile(
            "^([0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}$|^::1$|^::$");

    /**
     * Get client IP address from current request
     *
     * @return IP address string
     */
    public static String getIpAddr() {
        return getIpAddr(ServletUtil.getRequest());
    }

    /**
     * Get client IP address from HttpServletRequest
     * Enhanced with proper header checking and fallback logic
     *
     * @param request HTTP servlet request
     * @return IP address string
     */
    public static String getIpAddr(HttpServletRequest request) {
        if (request == null) {
            return "unknown";
        }

        String ip = request.getHeader("x-forwarded-for");
        if (StrUtil.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (StrUtil.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Forwarded-For");
        }
        if (StrUtil.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StrUtil.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (StrUtil.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        // Handle multiple IPs in x-forwarded-for header (take first one)
        if (StrUtil.isNotBlank(ip) && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }

        // Handle IPv6 localhost to IPv4 conversion
        return isLocalhost(ip) ? "127.0.0.1" : ip;
    }

    /**
     * Get the real IP address of the current server
     *
     * @return Server's public IP address
     */
    public static String getServerIp() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (Exception e) {
            log.warn("Failed to get local IP address", e);
            return "127.0.0.1";
        }
    }

    /**
     * Check if IP is internal/private network address
     * Simplified using Hutool NetUtil
     *
     * @param ip IP address to check
     * @return true if internal IP, false otherwise
     */
    public static boolean isInternalIp(String ip) {
        if (StrUtil.isBlank(ip)) {
            return false;
        }
        return NetUtil.isInnerIP(ip);
    }

    /**
     * Check if IP is localhost address
     *
     * @param ip IP address to check
     * @return true if localhost, false otherwise
     */
    public static boolean isLocalhost(String ip) {
        if (StrUtil.isBlank(ip)) {
            return false;
        }
        return "127.0.0.1".equals(ip) || "0:0:0:0:0:0:0:1".equals(ip) || "::1".equals(ip) || "localhost".equalsIgnoreCase(ip);
    }

    /**
     * Validate IP address format (IPv4 and IPv6)
     *
     * @param ip IP address to validate
     * @return true if valid IP format, false otherwise
     */
    public static boolean isValidIp(String ip) {
        if (StrUtil.isBlank(ip)) {
            return false;
        }
        return isValidIPv4(ip) || isValidIPv6(ip);
    }

    /**
     * Validate IPv4 address format
     *
     * @param ip IPv4 address to validate
     * @return true if valid IPv4 format, false otherwise
     */
    public static boolean isValidIPv4(String ip) {
        if (StrUtil.isBlank(ip)) {
            return false;
        }
        return IPV4_PATTERN.matcher(ip).matches();
    }

    /**
     * Validate IPv6 address format
     *
     * @param ip IPv6 address to validate
     * @return true if valid IPv6 format, false otherwise
     */
    public static boolean isValidIPv6(String ip) {
        if (StrUtil.isBlank(ip)) {
            return false;
        }
        return IPV6_PATTERN.matcher(ip).matches();
    }

    /**
     * Get IP geolocation information
     * Uses multiple API providers for better reliability
     *
     * @param ip IP address to lookup
     * @return Location information string (format: "Country, Province, City")
     */
    public static String getIpLocation(String ip) {
        if (StrUtil.isBlank(ip)) {
            return "Unknown";
        }

        // Skip internal IPs
        if (isInternalIp(ip)) {
            return "Internal Network";
        }

        try {
            // Try pconline API first (good for Chinese IPs)
            String location = getLocationFromPconline(ip);
            if (StrUtil.isNotBlank(location) && !"Unknown".equals(location)) {
                return location;
            }

            // Fallback to ip-api.com
            location = getLocationFromIpApi(ip);
            if (StrUtil.isNotBlank(location) && !"Unknown".equals(location)) {
                return location;
            }

        } catch (Exception e) {
            log.warn("Failed to get IP location for: {}", ip, e);
        }

        return "Unknown";
    }

    /**
     * Get location from pconline API (best for Chinese IP addresses)
     */
    private static String getLocationFromPconline(String ip) {
        try {
            String url = IP_API_URL3 + ip + "&json=true";
            String response = HttpUtil.get(url, 3000);

            if (StrUtil.isNotBlank(response)) {
                // Parse JSON response
                response = response.trim();
                if (response.contains("\"ip\"")) {
                    // Simple extraction - in production, use proper JSON parsing
                    int addrStart = response.indexOf("\"addr\":\"") + 8;
                    int addrEnd = response.indexOf("\"", addrStart);
                    if (addrEnd > addrStart) {
                        return response.substring(addrStart, addrEnd);
                    }
                }
            }
        } catch (Exception e) {
            log.debug("Pconline API failed for IP: {}", ip);
        }
        return null;
    }

    /**
     * Get location from ip-api.com (international coverage)
     */
    private static String getLocationFromIpApi(String ip) {
        try {
            String url = IP_API_URL1 + ip;
            String response = HttpUtil.get(url, 3000);

            if (StrUtil.isNotBlank(response) && response.contains("\"status\":\"success\"")) {
                // Extract country, regionName, city
                String country = extractJsonValue(response, "country");
                String region = extractJsonValue(response, "regionName");
                String city = extractJsonValue(response, "city");

                StringBuilder location = new StringBuilder();
                if (StrUtil.isNotBlank(country)) {
                    location.append(country);
                }
                if (StrUtil.isNotBlank(region)) {
                    if (!location.isEmpty()) {
                        location.append(", ");
                    }
                    location.append(region);
                }
                if (StrUtil.isNotBlank(city)) {
                    if (!location.isEmpty()) {
                        location.append(", ");
                    }
                    location.append(city);
                }

                return !location.isEmpty() ? location.toString() : null;
            }
        } catch (Exception e) {
            log.debug("IP-API failed for IP: {}", ip);
        }
        return null;
    }

    /**
     * Helper method to extract JSON value
     */
    private static String extractJsonValue(String json, String key) {
        String searchPattern = "\"" + key + "\":\"";
        int start = json.indexOf(searchPattern);
        if (start == -1) {
            return null;
        }
        start += searchPattern.length();
        int end = json.indexOf("\"", start);
        return end > start ? json.substring(start, end) : null;
    }

    /**
     * Get hostname for given IP address
     *
     * @param ip IP address
     * @return Hostname or IP if resolution fails
     */
    public static String getHostName(String ip) {
        if (StrUtil.isBlank(ip)) {
            return "unknown";
        }

        try {
            InetAddress address = InetAddress.getByName(ip);
            return address.getHostName();
        } catch (UnknownHostException e) {
            log.debug("Failed to resolve hostname for IP: {}", ip);
            return ip;
        }
    }

    /**
     * Check if two IPs are in the same network segment
     *
     * @param ip1 First IP address
     * @param ip2 Second IP address
     * @param subnetMask Subnet mask (e.g., "255.255.255.0")
     * @return true if in same network segment, false otherwise
     */
    public static boolean isSameNetwork(String ip1, String ip2, String subnetMask) {
        try {
            if (!isValidIPv4(ip1) || !isValidIPv4(ip2) || !isValidIPv4(subnetMask)) {
                return false;
            }

            long ip1Long = ipToLong(ip1);
            long ip2Long = ipToLong(ip2);
            long maskLong = ipToLong(subnetMask);

            return (ip1Long & maskLong) == (ip2Long & maskLong);
        } catch (Exception e) {
            log.warn("Failed to compare network segment for IPs: {}, {}", ip1, ip2);
            return false;
        }
    }

    /**
     * Convert IPv4 address to long value
     */
    private static long ipToLong(String ip) {
        String[] parts = ip.split("\\.");
        long result = 0;
        for (int i = 0; i < 4; i++) {
            result += Long.parseLong(parts[i]) << (24 - (8 * i));
        }
        return result;
    }

    /**
     * Convert IPv6 to IPv4 format (if applicable)
     *
     * @param ip IP address (IPv4 or IPv6)
     * @return IPv4 address or original if conversion not possible
     */
    public static String ipv6ToIpv4(String ip) {
        if (StrUtil.isBlank(ip)) {
            return ip;
        }

        // Handle IPv6 localhost
        if ("0:0:0:0:0:0:0:1".equals(ip) || "::1".equals(ip)) {
            return "127.0.0.1";
        }

        return ip;
    }

}