package com.ykuee.datamaintenance.common.base.constant;

public class Constant {

    private Constant() {}

    /**
     * redis-OK
     */
    public static final String OK = "OK";

    /**
     * redis过期时间，以秒为单位，一分钟
     */
    public static final int EXRP_MINUTE = 60;

    /**
     * redis过期时间，以秒为单位，一小时
     */
    public static final int EXRP_HOUR = 60 * 60;

    /**
     * redis过期时间，以秒为单位，一天
     */
    public static final int EXRP_DAY = 60 * 60 * 24;

    /**
     * redis-key-前缀-shiro:cache:
     */
    public static final String PREFIX_SHIRO_CACHE = "shiro:cache:";


    public static final String PREFIX_SHIRO = "shiro:";

    /**
     * redis-key-前缀-shiro:access_token:
     */
    public static final String PREFIX_SHIRO_ACCESS_TOKEN = "shiro:access_token:";

    /**
     * redis-key-前缀-shiro:refresh_token:
     */
    public static final String PREFIX_SHIRO_REFRESH_TOKEN = "shiro:refresh_token:";

    /**
     * JWT-loginName:
     */
    public static final String LOGIN_NAME = "loginName";

    /**
     * JWT-loginName:
     */
    public static final String USER_ID = "userId";

    /**
     * JWT-currentTimeMillis:
     */
    public static final String CURRENT_TIME_MILLIS = "currentTimeMillis";

    /**
     * PASSWORD_MAX_LEN
     */
    public static final Integer PASSWORD_MAX_LEN = 8;

    /**
     * 用户密码正则校验
     */
    public static final String PASSWORD_PATTERN = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,16}$";

    public static final String AUTHORIZATION = "Authorization";

    public static final String ACCESS_HEADERS = "Access-Control-Expose-Headers";

    public static final String OPERATE_NAME = "测试项目";

    public static final Boolean FLAG = false;

    public static final String ONE_LEVEL="1";

    public static final String TWO_LEVEL="2";

    public static final String EASY_CAPTCA="EASY_CAPTCA";


}
