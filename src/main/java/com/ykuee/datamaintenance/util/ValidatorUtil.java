package com.ykuee.datamaintenance.util;

import java.util.regex.Pattern;

import com.ykuee.datamaintenance.common.base.exception.BusinessException;

import cn.hutool.core.util.StrUtil;

/**
 * @author wmj
 * @ClassName ValidatorUtil.java
 * @Description 校验工具类
 * @createTime 2021年04月27日 10:24:00
 */
public class ValidatorUtil {

    /**
     * 正则表达式：校验vin码输入格式,只能是字母和数字
     */
    public static final String REGEX_VIN_INPUT = "^[a-zA-Z0-9]+";

    /**
     * 正则表达式：校验vin码不能包含字母I、O、Q
     */
    public static final String REGEX_VIN_CONTAIN = ".*[IOQ]+.*";

    /**
     * 正则表达式：校验vin码不能包含5位及以上连续相同的字符
     */
    public static final String  REGEX_VIN_EQUAL= "^.*(.)\\1{4,}.*";


    /**
     * 校验vin码输入格式,只能是字母和数字
     *
     * @param vinStr
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isVinInput(String vinStr) {
          return Pattern.matches(REGEX_VIN_INPUT, vinStr);
    }

    /**
     * 校验vin码不能包含字母I、O、Q
     *
     * @param vinStr
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isVinContain(String vinStr) {
          return !Pattern.matches(REGEX_VIN_CONTAIN, vinStr);
    }

    /**
     * 校验vin码不能包含5位及以上连续相同的字符
     *
     * @param vinStr
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isVinEqual(String vinStr) {
        return  !Pattern.matches(REGEX_VIN_EQUAL, vinStr);
    }
    
    /**
     * 
      *<p>Title: validatorVin</p>
      *<p>Description: 校验vin码信息，校验不通过抛出BusinessException</p>
      * @author Ykuee
      * @date 2021-4-27 
      * @param vinStr
     */
    public static void validatorVin(String vinStr) {
    	vinStr = vinStr.toUpperCase();
    	String msg = "VIN码格式不正确！";
    	boolean pass = true;
    	if(StrUtil.length(vinStr)!=17) {
    		pass = false;
    		msg += "长度应为17位  ";
    	}
    	if(!isVinInput(vinStr)) {
    		pass = false;
    		msg += "只能是字母和数字  ";
    	}
    	if(!isVinContain(vinStr)) {
    		pass = false;
    		msg += "不能包含字母I、O、Q  ";
    	}
    	if(!isVinEqual(vinStr)) {
    		pass = false;
    		msg += "不能包含5位及以上连续相同的字符  ";
    	}
    	if(!pass) {
    		throw new BusinessException(msg);
    	}
    }
    
}
