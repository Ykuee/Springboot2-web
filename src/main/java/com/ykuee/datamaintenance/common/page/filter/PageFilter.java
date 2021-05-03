package com.ykuee.datamaintenance.common.page.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.ykuee.datamaintenance.common.page.context.PageContext;
import com.ykuee.datamaintenance.common.page.entity.Page;
import com.ykuee.datamaintenance.common.page.entity.Sort;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PageFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(PageFilter.class);

    private String pageNoKey = "pageNo";
    private String pageSizeKey = "pageSize";
    private String columnKey = "column";
    private String orderKey = "order";

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String pageNo = httpServletRequest.getParameter(pageNoKey);
            String pageSize = httpServletRequest.getParameter(pageSizeKey);
            String[] columns = httpServletRequest.getParameterValues(columnKey);
            String[] orders = httpServletRequest.getParameterValues(orderKey);

            Page page = null;
            if (StringUtils.hasLength(pageNo) && StringUtils.hasLength(pageSize)) {
                logger.debug("page param pageNo: [{}]     page: [{}]", pageNo, pageSize);

                page = new Page(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
            }
            if (!ObjectUtils.isEmpty(columns) && !ObjectUtils.isEmpty(orders)) {
                if (page == null) {
                    page = new Page();
                }
                List<Sort> sortList = new ArrayList<>();
                for (int i = 0; i < columns.length; i++) {
                    logger.debug("sort param column: [{}]     order: [{}]", columns[i], orders[i]);

                    Sort sort = new Sort(columns[i], orders[i]);
                    sortList.add(sort);
                }
                page.setSorts(sortList);
            }

            // page不等于null的时候才分页
            if (page != null) {
                PageContext.startPage(page);
            }

            filterChain.doFilter(httpServletRequest, response);
        } finally {
            PageContext.clearPage();
        }
    }

    @Override
    public void setEnvironment(Environment environment) {
        String page = environment.getProperty("dm.db.page");
        if (StringUtils.hasLength(page)) {
            String[] pages = page.split(",");
            this.pageNoKey = pages[0];
            this.pageSizeKey = pages[1];
        }

        String sort = environment.getProperty("dm.db.sort");
        if (StringUtils.hasLength(sort)) {
            String[] sorts = page.split(",");
            this.columnKey = sorts[0];
            this.orderKey = sorts[1];
        }
    }

    @Override
    public void destroy() {
        logger.info("page filter destroy!");
    }

}
