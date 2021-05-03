package com.ykuee.datamaintenance.common.support;

import java.io.UnsupportedEncodingException;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.ykuee.datamaintenance.common.base.constant.Constant;
import com.ykuee.datamaintenance.common.base.exception.BusinessException;
import com.ykuee.datamaintenance.common.shiro.prop.JWTProp;
import com.ykuee.datamaintenance.util.Base64ConvertUtil;

/**
 * JAVA-JWT工具类
 * @author Wang926454
 * @date 2018/8/30 11:45
 */
@Component
public class JwtUtil {

    /**
     * logger
     */
    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);

    private static JWTProp jwtProp;
    
    @Autowired
    public void setJwtProp(JWTProp jwtProp) {
    	JwtUtil.jwtProp = jwtProp;
    }
    /**
     * 
      *<p>Title: verify</p>
      *<p>Description: 校验token是否正确</p>
      * @author Ykuee
      * @date 2021-3-3 
      * @param token
      * @return
     */
    public static boolean verify(String token) {
        try {
            // 帐号加JWT私钥解密
            String secret = getClaim(token, Constant.LOGIN_NAME) + Base64ConvertUtil.decode(jwtProp.getJWTKey());
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm).build();
            verifier.verify(token);
            return true;
        } catch (UnsupportedEncodingException e) {
            logger.error("JWTToken认证解密出现UnsupportedEncodingException异常:{}", e.getMessage());
            throw new BusinessException("JWTToken认证解密出现UnsupportedEncodingException异常:" + e.getMessage());
        }
    }
    /**
     * 
      *<p>Title: getClaim</p>
      *<p>Description: 获得Token中的信息无需secret解密也能获得</p>
      * @author Ykuee
      * @date 2021-3-3 
      * @param token
      * @param claim
      * @return
     */
    public static String getClaim(String token, String claim) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            // 只能输出String类型，如果是其他类型返回null
            return jwt.getClaim(claim).asString();
        } catch (JWTDecodeException e) {
            logger.error("解密Token中的公共信息出现JWTDecodeException异常:{}", e.getMessage());
            throw new BusinessException("解密Token中的公共信息出现JWTDecodeException异常:" + e.getMessage());
        }
    }


    /**
     * 
      *<p>Title: sign</p>
      *<p>Description: 返回加密的Token</p>
      * @author Ykuee
      * @date 2021-3-3 
      * @param loginName
      * @param currentTimeMillis
      * @return
     */
    public static String sign(String userId,String loginName, String currentTimeMillis) {
        try {
            // 帐号加JWT私钥加密
            String secret = loginName + Base64ConvertUtil.decode(jwtProp.getJWTKey());
            // 此处过期时间是以毫秒为单位，所以乘以1000
            Date date = new Date(System.currentTimeMillis() + Long.parseLong(jwtProp.getAccessTokenExpireTime()) * 1000);
            Algorithm algorithm = Algorithm.HMAC256(secret);
            // 附带loginName帐号信息
            return JWT.create()
                    .withClaim(Constant.LOGIN_NAME, loginName)
                    .withClaim(Constant.USER_ID, userId)
                    .withClaim(Constant.CURRENT_TIME_MILLIS, currentTimeMillis)
                    .withExpiresAt(date)
                    .sign(algorithm);
        } catch (UnsupportedEncodingException e) {
            logger.error("JWTToken加密出现UnsupportedEncodingException异常:{}", e.getMessage());
            throw new BusinessException("JWTToken加密出现UnsupportedEncodingException异常:" + e.getMessage());
        }
    }
}
