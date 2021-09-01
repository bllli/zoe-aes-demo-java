package com.zuoshouyisheng;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;

public class ZoeCrypt {
    private String key;

    private ZoeCrypt() {
    }

    public static ZoeCrypt Create(String key) {
        ZoeCrypt p = new ZoeCrypt();
        p.key = key;
        return p;
    }

    public String encrypt(String value) {
        try {
            int ivSize = 16;
            byte[] initVector = new byte[ivSize];
            SecureRandom random = new SecureRandom();
            random.nextBytes(initVector);
            IvParameterSpec iv = new IvParameterSpec(initVector);
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

            byte[] encrypted = cipher.doFinal(value.getBytes());
            byte[] allByteArray = new byte[initVector.length + encrypted.length];
            ByteBuffer buff = ByteBuffer.wrap(allByteArray);
            buff.put(initVector);
            buff.put(encrypted);
            return Base64.getEncoder().encodeToString(buff.array());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public String decrypt(String encrypted) {
        try {
            byte[] e = Base64.getDecoder().decode(encrypted);
            byte[] initVector = Arrays.copyOfRange(e, 0, 16);
            byte[] ee = Arrays.copyOfRange(e, 16, e.length);
            IvParameterSpec iv = new IvParameterSpec(initVector);
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            byte[] original = cipher.doFinal(ee);
            return new String(original);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public static void main(String[] args) {
        String access_secret = "7a47673e3e9149c89a10ad4561702186";
        String raw_string = "这是一段需要被加密的文本 hahah test";
        // 加密
        ZoeCrypt c = ZoeCrypt.Create(access_secret);
        String encrypted_string = c.encrypt(raw_string);
        System.out.println("加密结果：" + encrypted_string);  // encrypted_string 为加密结果字符串

        // 验证 解密成功
        String decrypt_string = c.decrypt(encrypted_string);
        System.out.println("解密结果：" + decrypt_string);
        assert raw_string.equals(decrypt_string);

        // 验证 解密失败
        assert c.decrypt(encrypted_string + "xxx") == null;
    }
}
