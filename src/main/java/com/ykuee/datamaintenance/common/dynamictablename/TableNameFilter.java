package com.ykuee.datamaintenance.common.dynamictablename;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TableNameFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(TableNameFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(httpServletRequest, response);
        } finally {
            TableNameContextHolder.removeTableNameAttributes();
        }
    }

    @Override
    public void destroy() {
        logger.info("tableName filter destroy!");
    }

}
