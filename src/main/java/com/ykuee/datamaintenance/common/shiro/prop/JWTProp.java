package com.ykuee.datamaintenance.common.shiro.prop;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "jwt")
public class JWTProp {
	
	private String AESKey;
	private String AESIV;
	private String JWTKey;
	//AccessToken过期时间-5分钟-5*60(秒为单位)
	private String accessTokenExpireTime;
	//RefreshToken过期时间-30分钟-30*60(秒为单位)
	private String refreshTokenExpireTime;
	//Shiro缓存过期时间-5分钟-5*60(秒为单位)(一般设置与AccessToken过期时间一致)
	private String shiroCacheExpireTime;
	
	private boolean jwtFilter;

	public String getAccessTokenExpireTime() {
		return accessTokenExpireTime;
	}
	public void setAccessTokenExpireTime(String accessTokenExpireTime) {
		this.accessTokenExpireTime = accessTokenExpireTime;
	}
	public String getRefreshTokenExpireTime() {
		return refreshTokenExpireTime;
	}
	public void setRefreshTokenExpireTime(String refreshTokenExpireTime) {
		this.refreshTokenExpireTime = refreshTokenExpireTime;
	}
	public String getShiroCacheExpireTime() {
		return shiroCacheExpireTime;
	}
	public void setShiroCacheExpireTime(String shiroCacheExpireTime) {
		this.shiroCacheExpireTime = shiroCacheExpireTime;
	}
	
	public String getAESKey() {
		return AESKey;
	}
	public void setAESKey(String aESKey) {
		AESKey = aESKey;
	}
	public String getAESIV() {
		return AESIV;
	}
	public void setAESIV(String aESIV) {
		AESIV = aESIV;
	}
	public String getJWTKey() {
		return JWTKey;
	}
	public void setJWTKey(String jWTKey) {
		JWTKey = jWTKey;
	}
	public boolean isJwtFilter() {
		return jwtFilter;
	}
	public void setJwtFilter(boolean jwtFilter) {
		this.jwtFilter = jwtFilter;
	}
}
