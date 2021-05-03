package com.ykuee.datamaintenance.common.mybatisplus.typehandler;

import org.apache.ibatis.type.Alias;

/**
 * 仅仅用于like查询
 *
 */
@Alias("fullLike")
public class FullLikeTypeHandler extends BaseLikeTypeHandler {
    public FullLikeTypeHandler() {
        super(true, true);
    }
}
