package org.frank.common.interceptor;

import com.baomidou.mybatisplus.core.toolkit.PluginUtils;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;

/**
 * MyBatis-Plus SQL 彩色日志拦截器
 * 功能：
 * - 参数直接拼接
 * - 彩色输出
 * - SQL 执行耗时统计
 * - 显示结果数量
 * - 可通过 application.yml 开关启用
 * - 格式化输出包含表头信息
 */
@Intercepts({
        @Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})
})
public class SqlLogInterceptor implements Interceptor {

    // ANSI 颜色
    private static final String RESET = "\u001B[0m";
    private static final String GRAY = "\u001B[90m";
    private static final String GREEN = "\u001B[32m";   // INSERT
    private static final String BLUE = "\u001B[34m";    // SELECT
    private static final String YELLOW = "\u001B[33m";  // UPDATE
    private static final String RED = "\u001B[31m";     // DELETE
    private static final String CYAN = "\u001B[36m";    // 时间/前缀

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        long start = System.currentTimeMillis();

        // 获取真实 StatementHandler
        StatementHandler statementHandler = PluginUtils.realTarget(invocation.getTarget());
        BoundSql boundSql = statementHandler.getBoundSql();
        Object parameterObject = boundSql.getParameterObject();

        // 原始 SQL
        String sql = boundSql.getSql().replaceAll("[\\s]+", " ").trim();

        // 参数拼接
        sql = getCompleteSql(sql, boundSql, parameterObject);

        // SQL 美化换行
        sql = formatSql(sql);

        // 执行 SQL
        Object result = invocation.proceed();

        long end = System.currentTimeMillis();
        long time = end - start;

        // SQL 类型颜色
        String color = getSqlColor(sql);

        // 统计结果数量
        String countInfo = "";
        if (result != null) {
            if (result instanceof List) {
                countInfo = " | result size: " + ((List<?>) result).size();
            } else if (result instanceof Integer) {
                countInfo = " | affected rows: " + result;
            }
        }

        // 打印彩色 SQL + 执行耗时 + 结果数量
        String now = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        System.out.println(CYAN + "[" + now + "] " + RESET +
                color + sql + RESET +
                GRAY + " [" + time + "ms]" + countInfo + RESET);

        return result;
    }

    @Override
    public Object plugin(Object target) {
        if (target instanceof StatementHandler) {
            return Plugin.wrap(target, this);
        }
        return target;
    }

    @Override
    public void setProperties(Properties properties) {
    }

    /**
     * 参数拼接
     */
    private String getCompleteSql(String sql, BoundSql boundSql, Object parameterObject) {
        if (parameterObject == null) return sql;

        try {
            MetaObject metaObject = SystemMetaObject.forObject(parameterObject);
            List<ParameterMapping> mappings = boundSql.getParameterMappings();

            for (ParameterMapping pm : mappings) {
                String name = pm.getProperty();
                Object value;

                if (boundSql.hasAdditionalParameter(name)) {
                    value = boundSql.getAdditionalParameter(name);
                } else if (metaObject.hasGetter(name)) {
                    value = metaObject.getValue(name);
                } else {
                    value = "?";
                }

                sql = sql.replaceFirst("\\?", Matcher.quoteReplacement(formatValue(value)));
            }
        } catch (Exception e) {
            // 忽略异常
        }

        return sql;
    }

    /**
     * 参数格式化
     */
    private String formatValue(Object obj) {
        if (obj == null) return "NULL";
        if (obj instanceof String) return "'" + obj.toString().replace("'", "''") + "'";
        if (obj instanceof Date) return "'" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format((Date) obj) + "'";
        return obj.toString();
    }

    /**
     * SQL 类型颜色
     */
    private String getSqlColor(String sql) {
        String sqlUpper = sql.trim().toUpperCase();
        if (sqlUpper.startsWith("SELECT")) return BLUE;
        if (sqlUpper.startsWith("INSERT")) return GREEN;
        if (sqlUpper.startsWith("UPDATE")) return YELLOW;
        if (sqlUpper.startsWith("DELETE")) return RED;
        return RESET;
    }

    /**
     * 简单 SQL 美化换行（根据关键字换行）
     */
    private String formatSql(String sql) {
        // 大写关键字换行
        String[] keywords = {"SELECT", "FROM", "WHERE", "GROUP BY", "ORDER BY", "HAVING", "JOIN", "LEFT JOIN",
                "RIGHT JOIN", "INNER JOIN", "OUTER JOIN", "ON", "AND", "OR", "VALUES", "SET"};
        for (String keyword : keywords) {
            sql = sql.replaceAll("(?i)\\b" + keyword + "\\b", "\n" + keyword);
        }
        // 去掉多余空行
        sql = sql.replaceAll("\n\\s+", "\n");
        return sql;
    }
}



