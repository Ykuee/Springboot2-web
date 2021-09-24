package com.ykuee.datamaintenance.common.response.page.interceptor;

import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;

import com.ykuee.datamaintenance.common.response.page.context.PageContextHolder;
import com.ykuee.datamaintenance.common.response.page.entity.Page;

import java.util.List;
import java.util.Properties;

/**
 * 分页拦截器
 *
 */
@Intercepts({
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class})
})
public class PageInterceptor implements Interceptor {

    private final Logger log = LoggerFactory.getLogger(PageInterceptor.class);

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object[] args = invocation.getArgs();

        Object object = invocation.proceed();
        if (!ObjectUtils.isEmpty(args) && args.length == 6) {
            MappedStatement mappedStatement = (MappedStatement) args[0];
            BoundSql boundSql = (BoundSql) args[5];

            String methodName = mappedStatement.getId();
            String sql = boundSql.getSql();
            Page page = PageContextHolder.getPageAttributes();

            if (methodName.endsWith("_COUNT") && sql.contains("count(0)") && page != null) {
                List<Long> totalList = (List<Long>) object;
                Long total = totalList.get(0);
                PageContextHolder.getPageAttributes().setTotal(total);
                log.info("page total: [{}]", total);
            }
        }
        return object;
    }

    /**
     * 拦截类型StatementHandler
     */
    @Override
    public Object plugin(Object target) {
        if (target instanceof Executor) {
            return Plugin.wrap(target, this);
        }
        return target;
    }

    @Override
    public void setProperties(Properties properties) {

    }
}
