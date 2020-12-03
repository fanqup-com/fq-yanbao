package com.fanqing.util;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class Base64Util {

    /**
     * BASE64解密
     *
     * @param key
     * @return
     * @throws Exception
     */
    public static String decoderBASE64(String key) throws Exception {
        return new String((new BASE64Decoder()).decodeBuffer(key));
    }

    /**
     * BASE64加密
     *
     * @param key
     * @return
     * @throws Exception
     */
    public static String encoderBASE64(String key) throws Exception {
        return (new BASE64Encoder()).encodeBuffer(key.getBytes());
    }
}
