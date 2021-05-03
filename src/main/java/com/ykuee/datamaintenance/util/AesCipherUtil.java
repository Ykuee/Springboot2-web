package com.ykuee.datamaintenance.util;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.ykuee.datamaintenance.common.base.exception.BusinessException;

/**
 * 
  * @version:
  * @Description: AES加密解密工具类
  * @author: Ykuee
  * @date: 2021-3-3 15:32:45
 */
@Component
public class AesCipherUtil {

    /**
     * AES密码加密私钥(Base64加密)
     */
    private static String KEY;
    
    private static String IV;
    // private static final byte[] KEY = { 1, 1, 33, 82, -32, -85, -128, -65 };

    @Value("${jwt.AESKey}")
    public void setEncryptAESKey(String KEY) {
        AesCipherUtil.KEY = KEY;
    }
    
    @Value("${jwt.AESIV}")
    public void setEncryptAESVi(String IV) {
    	AesCipherUtil.IV = IV;
    }

    /**
     * logger
     */
    private static final Logger logger = LoggerFactory.getLogger(AesCipherUtil.class);

    public static String enCrypto(String data) {
        try {
        	 Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            byte[] plaintext = data.getBytes("UTF-8");

            SecretKeySpec keyspec = new SecretKeySpec(KEY.getBytes(), "AES");
            IvParameterSpec ivspec = new IvParameterSpec(IV.getBytes());

            cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);
            byte[] encrypted = cipher.doFinal(plaintext);

            return new Base64().encodeToString(encrypted);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            logger.error("getInstance()方法异常:{}", e.getMessage());
            e.printStackTrace();
            throw new BusinessException("getInstance()方法异常:" + e.getMessage());
        } catch (UnsupportedEncodingException e) {
            logger.error("Base64加密异常:{}", e.getMessage());
            e.printStackTrace();
            throw new BusinessException("Base64加密异常:" + e.getMessage());
        } catch (InvalidKeyException e) {
            logger.error("初始化Cipher对象异常:{}", e.getMessage());
            e.printStackTrace();
            throw new BusinessException("初始化Cipher对象异常:" + e.getMessage());
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            logger.error("加密异常，密钥有误:{}", e.getMessage());
            e.printStackTrace();
            throw new BusinessException("加密异常，密钥有误:" + e.getMessage());
        } catch (InvalidAlgorithmParameterException e) {
        	logger.error("初始化Cipher对象异常:{}", e.getMessage());
        	e.printStackTrace();
        	throw new BusinessException("初始化Cipher对象异常:" + e.getMessage());
		}
    }

    public static String deCrypto(String data) {
        try {
        	byte[] encrypted1 = new Base64().decode(data);
    		
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            SecretKeySpec keyspec = new SecretKeySpec(KEY.getBytes(), "AES");
            IvParameterSpec ivspec = new IvParameterSpec(IV.getBytes());

            cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);

            byte[] original = cipher.doFinal(encrypted1);
            String originalString = new String(original, "UTF-8");
            return originalString;
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            logger.error("getInstance()方法异常:{}", e.getMessage());
            throw new BusinessException("getInstance()方法异常:" + e.getMessage());
        } catch (UnsupportedEncodingException e) {
            logger.error("Base64解密异常:{}", e.getMessage());
            throw new BusinessException("Base64解密异常:" + e.getMessage());
        } catch (InvalidKeyException e) {
            logger.error("初始化Cipher对象异常:{}", e.getMessage());
            throw new BusinessException("初始化Cipher对象异常:" + e.getMessage());
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            logger.error("解密异常，密钥有误:{}", e.getMessage());
            throw new BusinessException("解密异常，密钥有误:" + e.getMessage());
        } catch (InvalidAlgorithmParameterException e) {
        	logger.error("初始化Cipher对象异常:{}", e.getMessage());
            throw new BusinessException("初始化Cipher对象异常:" + e.getMessage());
		}
    }
    
    public static void main(String[] args) {
    	AesCipherUtil util = new AesCipherUtil();
    	util.setEncryptAESKey("DMYkUeewwTzHaCo1");
    	util.setEncryptAESVi("aV6jWQRQ1TzsrLKe");
    	String s = util.enCrypto("admin3123456");
    	System.out.println(s);
    	System.out.println(util.deCrypto(s));
	}
}
