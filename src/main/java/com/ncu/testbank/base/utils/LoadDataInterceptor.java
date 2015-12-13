package com.ncu.testbank.base.utils;

import java.io.InputStream;
import java.sql.Statement;
import java.util.Properties;
 
import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;

@Intercepts({ @Signature(method = "update", type = StatementHandler.class, args = { Statement.class }) })
public class LoadDataInterceptor implements Interceptor {
 
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        RoutingStatementHandler handler = (RoutingStatementHandler) invocation.getTarget();
        BoundSql boundSql = handler.getBoundSql();
        if (boundSql.getSql().toLowerCase().contains("LOAD DATA LOCAL INFILE")) {
            Object in = boundSql.getParameterObject();
            if (in != null && in instanceof InputStream) { // 如果不使用流的方式， 则会读取语句中对应在本地的文件
                Statement statement = (Statement) invocation.getArgs()[0];
                if (statement.isWrapperFor(com.mysql.jdbc.Statement.class)) {
                    com.mysql.jdbc.PreparedStatement mysqlStatement = statement.unwrap(com.mysql.jdbc.PreparedStatement.class);
                    // 将流设置到执行语句中，在后续执行过程中，会忽略load data 语句中的文件名，改用当前设置流
                    mysqlStatement.setLocalInfileInputStream((InputStream)in);
                    invocation.getArgs()[0] = mysqlStatement; // 将当前语句执行代理，换成mysql的语句对象，方便下面执行。
                }
            }
        }
        return invocation.proceed();
    }
 
    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }
 
    @Override
    public void setProperties(Properties properties) {
 
    }
 
}