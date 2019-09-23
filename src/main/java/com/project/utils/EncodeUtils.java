package com.project.utils;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * 加密
 */
public class EncodeUtils {
	/**
	 * AES加密
	 */
	public static String aesEncode(String content, String key){
		// 创建密码器
		Cipher cipher;
		try {
			cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			SecretKeySpec secretKeySpec = new SecretKeySpec(MD5Encode(key).toLowerCase().getBytes(), "AES");
			// 初始化
			cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
			return new String(Base64.encodeBase64(cipher.doFinal(content.getBytes())));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("aes加密异常");
		}

	}

	/**
	 * AES解密
	 */
	public static String aesDecode(String content){
		return aesDecode(content, null);
	}

	/**
	 * AES解密
	 */
	public static String aesDecode(String content, String key){
		Cipher cipher;
		try {
			cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			SecretKeySpec secretKeySpec = new SecretKeySpec(MD5Encode(key).toLowerCase().getBytes(), "AES");
			cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
			return new String(cipher.doFinal(Base64.decodeBase64(content.getBytes("utf-8"))));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("aes解密失败");
		}
	}

	/**
	 * md5加密
	 */
	public static String MD5Encode(String content, String key) {
		return DigestUtils.md5Hex(content + key);
	}

	/**
	 * md5加密
	 */
	public static String MD5Encode(String content){
		return DigestUtils.md5Hex(content);
	}

	/**
	 * base64加密
	 */
	public static String base64Encode(String content){
		try {
			return new String(Base64.encodeBase64(content.getBytes("utf-8")), "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			throw new RuntimeException("加密失败");
		}
	}

	/**
	 * base64解码
	 */
	public static String base64Decode(String content){
		try {
			return new String(Base64.decodeBase64(content.getBytes("utf-8")), "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			throw new RuntimeException("加密失败");
		}
	}

	/**
	 * sha1(哈希值校验)
	 */
	public static String sha1(String data){
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("SHA1");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			throw new RuntimeException("哈希值校验异常");
		}
		md.update(data.getBytes());
		StringBuilder buf = new StringBuilder();
		byte[] bits = md.digest();
		for (byte bit : bits) {
			int a = bit;
			if (a < 0)
				a += 256;
			if (a < 16)
				buf.append("0");
			buf.append(Integer.toHexString(a));
		}
		return buf.toString();
	}

	/**
	 * aes对称加密
	 */
	public static String aesEncode(String content) {
		//加密秘钥
		String encodeRules = "987654321";
		try {
			//构造密钥生成器，指定为AES算法,不区分大小写
			KeyGenerator keygen = KeyGenerator.getInstance("AES");
			//根据传入的字节数组,生成一个128位的随机源
			SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG" );
			secureRandom.setSeed(encodeRules.getBytes());
			keygen.init(128, secureRandom);
			//产生原始对称密钥
			SecretKey original_key = keygen.generateKey();
			//获得原始对称密钥的字节数组
			byte[] raw = original_key.getEncoded();
			//根据字节数组生成AES密钥
			SecretKey key = new SecretKeySpec(raw, "AES");
			//根据指定算法AES自成密码器
			Cipher cipher = Cipher.getInstance("AES");
			//初始化密码器，第一个参数为加密(Encrypt_mode)或者解密解密(Decrypt_mode)操作，第二个参数为使用的KEY
			cipher.init(Cipher.ENCRYPT_MODE, key);
			//获取加密内容的字节数组(这里要设置为utf-8)不然内容中如果有中文和英文混合中文就会解密为乱码
			byte[] byte_encode = content.getBytes("utf-8");
			//根据密码器的初始化方式--加密：将数据加密
			byte[] byte_AES = cipher.doFinal(byte_encode);
			//将加密后的数据转换为字符串并返回
			return new String(Base64.encodeBase64(byte_AES));
		} catch (Exception e) {
			e.printStackTrace();
		}
		//如果有错就返回null
		return null;
	}
}
