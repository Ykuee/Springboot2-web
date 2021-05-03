package com.ykuee.datamaintenance.util;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

/**
 * 
  * @version:
  * @Description: Base64工具
  * @author: Ykuee
  * @date: 2021-3-3 15:33:11
 */
public class Base64ConvertUtil {

    private Base64ConvertUtil() {}

    /**
     * 
      *<p>Title: encode</p>
      *<p>Description: 加密JDK1.8</p>
      * @author Ykuee
      * @date 2021-3-3 
      * @param str
      * @return
      * @throws UnsupportedEncodingException
     */
    public static String encode(String str) throws UnsupportedEncodingException {
        byte[] encodeBytes = Base64.getEncoder().encode(str.getBytes("utf-8"));
        return new String(encodeBytes);
    }

    /**
     * 
      *<p>Title: decode</p>
      *<p>Description: 解密JDK1.8</p>
      * @author Ykuee
      * @date 2021-3-3 
      * @param str
      * @return
      * @throws UnsupportedEncodingException
     */
    public static String decode(String str) throws UnsupportedEncodingException {
        byte[] decodeBytes = Base64.getDecoder().decode(str.getBytes("utf-8"));
        return new String(decodeBytes);
    }

}
