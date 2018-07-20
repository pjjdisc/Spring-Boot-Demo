package com.demo.units;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;

/**
 * @author Eddy
 * @version V1.0
 * @Date 2018-07-20 15:46
 */
public class Md5Unit {
    private static final String UTF8 = "utf-8";
    private static final Logger logger = LoggerFactory.getLogger(Md5Unit.class);
    public static String md5(String str){
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] bytes = md.digest(str.getBytes(UTF8));
            final char[] HEX_DIGITS = "0123456789ABCDEF".toCharArray();
            StringBuilder ret = new StringBuilder(bytes.length * 2);
            for (byte aByte : bytes) {
                ret.append(HEX_DIGITS[(aByte >> 4) & 0x0f]);
                ret.append(HEX_DIGITS[aByte & 0x0f]);
            }
            return ret.toString();
        } catch (Exception ex){
            logger.error(ex.getMessage());
            return "";
        }
    }
}
