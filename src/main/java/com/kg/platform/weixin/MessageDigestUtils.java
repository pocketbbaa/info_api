package com.kg.platform.weixin;

import java.security.MessageDigest;

public class MessageDigestUtils {

    public static String encrypt(String password, String algorithm) throws Exception {
        MessageDigest md = MessageDigest.getInstance(algorithm);
        byte[] b = md.digest(password.getBytes());
        return ByteUtils.byte2HexString(b);
    }
}
