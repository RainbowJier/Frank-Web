package org.frank.common.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.*;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@Intercepts({
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})
})
public class SqlLogInterceptor implements Interceptor {

    private enum SqlColor {
        RESET("\u001B[0m"), BLUE("\u001B[34m"), GREEN("\u001B[32m"),
        YELLOW("\u001B[33m"), RED("\u001B[31m"), CYAN("\u001B[36m"), WHITE("\u001B[37m");

        public final String v;
        SqlColor(String v) { this.v = v; }
    }

    private static final SimpleDateFormat DF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        long start = System.currentTimeMillis();

        MappedStatement ms = (MappedStatement) invocation.getArgs()[0];
        Object param = invocation.getArgs()[1];
        BoundSql boundSql = ms.getBoundSql(param);

        String sql = formatSql(bindSql(boundSql));
        String method = getMethodName(ms.getId());
        SqlColor color = getColor(sql);

        Object result = invocation.proceed();
        long cost = System.currentTimeMillis() - start;

        String now = DF.format(new Date());
        String resultInfo = getResultInfo(result);

        String timeColor = cost < 100 ? SqlColor.GREEN.v : (cost < 500 ? SqlColor.YELLOW.v : SqlColor.RED.v);

        StringBuilder sb = new StringBuilder(256)
                .append(SqlColor.CYAN.v).append("\n━━━━━━━━━━━ SQL LOG ━━━━━━━━━━━\n")
                .append("Time: ").append(now).append("\n")
                .append("Method: ").append(SqlColor.WHITE.v).append(method).append("\n")
                .append("SQL: ").append(color.v).append(sql).append(SqlColor.RESET.v).append("\n")
                .append("Cost: ").append(timeColor).append(cost).append("ms").append(SqlColor.RESET.v)
                .append(resultInfo).append("\n")
                .append("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n")
                .append(SqlColor.RESET.v);

        System.out.println(sb);
        return result;
    }

    /** 绑定 SQL 参数并输出完整 SQL */
    private String bindSql(BoundSql boundSql) {
        List<ParameterMapping> pm = boundSql.getParameterMappings();
        Object paramObj = boundSql.getParameterObject();
        if (pm == null || pm.isEmpty()) return boundSql.getSql();

        MetaObject meta = SystemMetaObject.forObject(paramObj);
        String sql = boundSql.getSql();
        StringBuilder result = new StringBuilder();
        int paramIndex = 0;

        // 使用String的indexOf和substring来处理，避免正则表达式的问题
        for (int i = 0; i < sql.length(); i++) {
            char c = sql.charAt(i);
            if (c == '?' && paramIndex < pm.size()) {
                ParameterMapping mapping = pm.get(paramIndex);
                String property = mapping.getProperty();
                Object value = boundSql.hasAdditionalParameter(property)
                        ? boundSql.getAdditionalParameter(property)
                        : meta.hasGetter(property) ? meta.getValue(property) : null;

                result.append(formatValue(value));
                paramIndex++;
            } else {
                result.append(c);
            }
        }

        return result.toString();
    }

    /** 格式 SQL */
    private String formatSql(String sql) {
        return sql.replaceAll("\n", " ")
                .replaceAll("\\s+", " ")
                .replaceAll("(?i)select ", "\nSELECT ")
                .replaceAll("(?i) from ", "\nFROM ")
                .replaceAll("(?i) where ", "\nWHERE ")
                .replaceAll("(?i) order by ", "\nORDER BY ")
                .replaceAll("(?i) group by ", "\nGROUP BY ")
                .trim();
    }

    private String formatValue(Object v) {
        if (v == null) return "NULL";
        if (v instanceof String) return "'" + v.toString().replace("'", "''") + "'";
        if (v instanceof Date) return "'" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(v) + "'";
        if (v instanceof Boolean) return (Boolean) v ? "1" : "0";
        if (v instanceof Collection<?>) {
            StringBuilder sb = new StringBuilder("(");
            Iterator<?> it = ((Collection<?>) v).iterator();
            while (it.hasNext()) sb.append(formatValue(it.next())).append(it.hasNext() ? "," : "");
            return sb.append(")").toString();
        }
        return v.toString();
    }

    private SqlColor getColor(String sql) {
        sql = sql.trim().toUpperCase(Locale.ROOT);
        if (sql.startsWith("SELECT")) return SqlColor.BLUE;
        if (sql.startsWith("INSERT")) return SqlColor.GREEN;
        if (sql.startsWith("UPDATE")) return SqlColor.YELLOW;
        if (sql.startsWith("DELETE")) return SqlColor.RED;
        return SqlColor.RESET;
    }

    private String getMethodName(String id) {
        int dot = id.lastIndexOf('.');
        return dot != -1 ? id.substring(dot + 1) : id;
    }

    private String getResultInfo(Object r) {
        if (r instanceof Collection<?>) return " | rows: " + ((Collection<?>) r).size();
        if (r instanceof Integer) return " | affected: " + r;
        return "";
    }

    @Override
    public Object plugin(Object target) {
        return target instanceof Executor ? Plugin.wrap(target, this) : target;
    }

    @Override public void setProperties(Properties properties) {}
}
