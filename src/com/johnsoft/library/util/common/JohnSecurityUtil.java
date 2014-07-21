package com.johnsoft.library.util.common;

import java.io.ByteArrayOutputStream;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/** 
 * 搜集的java加密解密签名验证的辅助类<br>
 * 这里MessageDigest, KeyGenerator, KeyPairGenerator, KeyFactory, SecretKeyFactory, SecretKeySpec, Cipher, Signature
 * 需要的算法名字可从Java Cryptography Architecture Standard Algorithm Name Document中得到枚举<br>
 * 这里有两类返回值,正常值和非正常值,当非正常时,如果方法声明了抛出异常则抛出异常,否则以返回null视作操作失败.
 */
public class JohnSecurityUtil
{
	public static final String encodeBase64(byte[] bytes)
	{
		return new String(Base64.getEncoder().encode(bytes));
	}
	
	public static final byte[] decodeBase64(String str)
	{
		return Base64.getDecoder().decode(str);
	}
	
	public static final String encryptMD5(String message)
	{
		try
		{
			return byte2hex(encryptMD5(message.getBytes()));
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	/** 方便再加一个base64编码 */
	public static final byte[] encryptMD5(byte[] message) throws Exception
	{
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(message);
		MessageDigest mdc = (MessageDigest) md.clone();
		return mdc.digest();
	}

	public static final String encryptSHA1(String message)
	{
		try
		{
			return  byte2hex(encryptSHA1(message.getBytes()));
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	/** 方便再加一个base64编码 */
	public static final byte[] encryptSHA1(byte[] message) throws Exception
	{
		MessageDigest md = MessageDigest.getInstance("SHA-1");
		md.update(message);
		return md.digest();
	}

	public static final String encryptSHA256(String message)
	{
		try
		{
			return byte2hex(encryptSHA256(message.getBytes()));
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	/** 方便再加一个base64编码 */
	public static final byte[] encryptSHA256(byte[] message) throws Exception
	{
		return MessageDigest.getInstance("SHA-256").digest(message);
	}

	/** 字节转16进制字符串 */
	public static final String byte2hex(byte[] bytes)
	{
		String hs = "";
		String stmp = "";
		for (int i = 0; i < bytes.length; i++)
		{
			stmp = Integer.toHexString(bytes[i] & 0X000000FF);
			if (stmp.length() == 1)
				hs = hs + "0" + stmp;
			else
				hs = hs + stmp;
		}
		return hs.toUpperCase();
	}
	
    public static final String genHMACKey() throws Exception
    {  
    	KeyGenerator keyGenerator = KeyGenerator.getInstance("HmacSHA1");
		SecretKey secretKey = keyGenerator.generateKey();
		return encodeBase64(secretKey.getEncoded());
    }  
  
    /**
     * @param data 加密前的内容字节
     * @param HmacKey genHMACKey方法生成的密钥
     * @return 加密后内容字节
     */
    public static final byte[] encryptHMAC(byte[] data, String HmacKey) 
    {  
        try
		{
			SecretKey secretKey = new SecretKeySpec(decodeBase64(HmacKey), "HmacSHA1");
			Mac mac = Mac.getInstance(secretKey.getAlgorithm());
			mac.init(secretKey);
			return mac.doFinal(data);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}  
    }
    
    /**
	 * @param content 要加密的字符串
	 * @param password 加密密码
	 * @return 加密后内容字节
	 */
    public static final byte[] encryptDES(String content, String password)
	{
		try
		{
			KeyGenerator kgen = KeyGenerator.getInstance("DES");
			kgen.init(56, new SecureRandom(password.getBytes()));
			SecretKey secretKey = kgen.generateKey();
			byte[] enCodeFormat = secretKey.getEncoded();
			SecretKeySpec key = new SecretKeySpec(enCodeFormat, "DES");
			Cipher cipher = Cipher.getInstance("DES");
			byte[] byteContent = content.getBytes("utf-8");
			cipher.init(Cipher.ENCRYPT_MODE, key);
			byte[] result = cipher.doFinal(byteContent);
			return result;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

    /**
	 * @param content 加密后的内容字节
	 * @param password 加密密码
	 * @return 解密后的原字符串
	 */
	public static final String decryptDES(byte[] content, String password)
	{
		try
		{
			KeyGenerator kgen = KeyGenerator.getInstance("DES");
			kgen.init(56, new SecureRandom(password.getBytes()));
			SecretKey secretKey = kgen.generateKey();
			byte[] enCodeFormat = secretKey.getEncoded();
			SecretKeySpec key = new SecretKeySpec(enCodeFormat, "DES");
			Cipher cipher = Cipher.getInstance("DES");
			cipher.init(Cipher.DECRYPT_MODE, key);
			byte[] result = cipher.doFinal(content);
			return new String(result, "utf-8");
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * @param content 要加密的字符串
	 * @param password 加密密码
	 * @return 加密后内容字节
	 */
	public static final byte[] encryptAES(String content, String password)
	{
		try
		{
			KeyGenerator kgen = KeyGenerator.getInstance("AES");
			kgen.init(128, new SecureRandom(password.getBytes()));
			SecretKey secretKey = kgen.generateKey();
			byte[] enCodeFormat = secretKey.getEncoded();
			SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
			Cipher cipher = Cipher.getInstance("AES");
			byte[] byteContent = content.getBytes("utf-8");
			cipher.init(Cipher.ENCRYPT_MODE, key);
			byte[] result = cipher.doFinal(byteContent);
			return result;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * @param content 加密后的内容字节
	 * @param password 加密密码
	 * @return 解密后的原字符串
	 */
	public static final String decryptAES(byte[] content, String password)
	{
		try
		{
			KeyGenerator kgen = KeyGenerator.getInstance("AES");
			kgen.init(128, new SecureRandom(password.getBytes()));
			SecretKey secretKey = kgen.generateKey();
			byte[] enCodeFormat = secretKey.getEncoded();
			SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.DECRYPT_MODE, key);
			byte[] result = cipher.doFinal(content);
			return new String(result, "utf-8");
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * @param algorithm "AES"或"DES"
	 * @param seed 生成密钥的种子,可以传个密码进去
	 */
	public static final String genDESorAESkey(String algorithm, String seed) throws Exception 
	{  
        KeyGenerator kg = KeyGenerator.getInstance(algorithm);  
        kg.init(new SecureRandom(decodeBase64(seed)));  
        SecretKey secretKey = kg.generateKey();  
        return encodeBase64(secretKey.getEncoded());  
    }
	
	/**
	 * @param content 要加密内容字节
	 * @param algorithm "AES"或"DES"
	 * @param key genDESorAESkey方法产生的密钥
	 * @return 加密后内容字节
	 */
	public static final byte[] encryptDESorAES(byte[] content, String algorithm, String key) throws Exception
	{
		SecretKey secretKey = new SecretKeySpec(decodeBase64(key), algorithm);
		Cipher cipher = Cipher.getInstance(algorithm);
		cipher.init(Cipher.ENCRYPT_MODE, secretKey);
		return cipher.doFinal(content);
	}
	
	/**
	 * @param content 要解密内容字节
	 * @param algorithm "AES"或"DES"
	 * @param key genDESorAESkey方法产生的密钥
	 * @return 解密后内容字节
	 */
	public static final byte[] decryptDESorAES(byte[] content, String algorithm, String key) throws Exception
	{
		SecretKey secretKey = new SecretKeySpec(decodeBase64(key), algorithm);
		Cipher cipher = Cipher.getInstance(algorithm);
		cipher.init(Cipher.DECRYPT_MODE, secretKey);
		return cipher.doFinal(content);
	}
	
	 /** 获得一份盐用于PBE加解密 */
    public static final byte[] createSalt() throws Exception 
    {  
        byte[] salt = new byte[8];  
        Random random = new Random();  
        random.nextBytes(salt);  
        return salt;
    }  
  
    /**
     * PBE加密 
     * @param data 原内容字节 
     * @param password 密码 
     * @param salt 盐,可通过createSalt方法获得,须留存以便解密之需
     * @return 加密后内容字节
     */
    public static final byte[] encryptPBE(byte[] data, String password, byte[] salt)
    {
    	try
		{
			PBEKeySpec keySpec = new PBEKeySpec(password.toCharArray());
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithSHA1AndDESede");
			SecretKey secretKey = keyFactory.generateSecret(keySpec);
			PBEParameterSpec paramSpec = new PBEParameterSpec(salt, 100);
			Cipher cipher = Cipher.getInstance("PBEWithSHA1AndDESede");
			cipher.init(Cipher.ENCRYPT_MODE, secretKey, paramSpec);
			return cipher.doFinal(data);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}  
    }
  
    /**
     * PBE解密 
     * @param data 加密的内容字节
     * @param password 密码 
     * @param salt 盐,可通过createSalt方法获得,须留存以便解密之需 
     * @return 解密后内容字节
     */
    public static final byte[] decryptPBE(byte[] data, String password, byte[] salt)
    {
    	try
		{
			PBEKeySpec keySpec = new PBEKeySpec(password.toCharArray());
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithSHA1AndDESede");
			SecretKey secretKey = keyFactory.generateSecret(keySpec);
			PBEParameterSpec paramSpec = new PBEParameterSpec(salt, 100);
			Cipher cipher = Cipher.getInstance("PBEWithSHA1AndDESede");
			cipher.init(Cipher.DECRYPT_MODE, secretKey, paramSpec);
			return cipher.doFinal(data);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}  
    }
	
	 /** 
      *  生成密钥对(公钥和私钥) <br>
      *  注: <br>
      *  字符串格式的密钥在未在特殊说明情况下都为BASE64编码格式,<br>
      *  由于非对称加密速度极其缓慢，一般文件不使用它来加密而是使用对称加密,<br>
      *  非对称加密算法可以用来对对称加密的密钥加密，这样保证密钥的安全也就保证了数据的安全 <br>
      *  @param alg 加密算法, "RSA"或"DSA"
      */
    public static final KeyPairString genKeyPair(String alg) throws Exception 
    {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(alg);
        keyPairGen.initialize(1024, new SecureRandom());
        KeyPair keyPair = keyPairGen.generateKeyPair();
        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();
        KeyPairString keyPairStr = new KeyPairString();
        keyPairStr.privateKey = encodeBase64(privateKey.getEncoded());
        keyPairStr.publicKey = encodeBase64(publicKey.getEncoded());
        return keyPairStr;
    }
    
    /** 
     *  生成密钥对(公钥和私钥) <br>
     *  注: <br>
     *  字符串格式的密钥在未在特殊说明情况下都为BASE64编码格式,<br>
     *  由于非对称加密速度极其缓慢，一般文件不使用它来加密而是使用对称加密,<br>
     *  非对称加密算法可以用来对对称加密的密钥加密，这样保证密钥的安全也就保证了数据的安全 <br>
     *  @param alg 加密算法, "RSA"或"DSA"
     *  @param seed 种子 
     */
    public static final KeyPairString genKeyPair(String alg, String seed) throws Exception 
    {  
        KeyPairGenerator keygen = KeyPairGenerator.getInstance(alg);  
        SecureRandom secureRandom = new SecureRandom();
        if(seed == null) seed = "0f22507a10bbddd07d8a3082122966e3";
        secureRandom.setSeed(seed.getBytes());  
        keygen.initialize(1024, secureRandom);  
        KeyPair keyPair = keygen.genKeyPair();  
        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();  
        KeyPairString keyPairStr = new KeyPairString();
        keyPairStr.privateKey = encodeBase64(privateKey.getEncoded());
        keyPairStr.publicKey = encodeBase64(publicKey.getEncoded());
        return keyPairStr;
    }
    
    /**
     * <p>
     * RSA方式用私钥对信息生成数字签名
     * </p>
     * @param data 已加密数据
     * @param privateKey 私钥(BASE64编码) 由genKeyPair提供
     * @return 返回数字签名后字符串
     */
    public static final String signRSA(byte[] data, String privateKey)
    {
        try
		{
			byte[] keyBytes = decodeBase64(privateKey);
			PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			PrivateKey privateK = keyFactory.generatePrivate(pkcs8KeySpec);
			Signature signature = Signature.getInstance("SHA1withRSA");
			signature.initSign(privateK);
			signature.update(data);
			return encodeBase64(signature.sign());
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
    }

    /**
     * <p>
     * RSA方式用公钥校验数字签名
     * </p>
     * @param data 已加密数据
     * @param publicKey 公钥(BASE64编码) 由genKeyPair提供
     * @param sign 数字签名
     * @return 成功返回true,失败返回false
     */
    public static final boolean verifyRSA(byte[] data, String publicKey, String sign)
    {
        try
		{
			byte[] keyBytes = decodeBase64(publicKey);
			X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			PublicKey publicK = keyFactory.generatePublic(keySpec);
			Signature signature = Signature.getInstance("SHA1withRSA");
			signature.initVerify(publicK);
			signature.update(data);
			return signature.verify(decodeBase64(sign));
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
    }

    /**
     * @param encryptedData 已加密数据
     * @param privateKey 私钥(BASE64编码) 由genKeyPair提供
     * @return 私钥解密后数据
     */
    public static final byte[] decryptRSAByPrivateKey(byte[] encryptedData, String privateKey)
    {
        try
		{
			byte[] keyBytes = decodeBase64(privateKey);
			PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			PrivateKey privateK = keyFactory.generatePrivate(pkcs8KeySpec);
			Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
			cipher.init(Cipher.DECRYPT_MODE, privateK);
			int inputLen = encryptedData.length;
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			int offSet = 0;
			byte[] cache;
			final int maxDecryptBlock = 128;
			int i = 0;
			// 对数据分段解密
			while (inputLen - offSet > 0)
			{
				if (inputLen - offSet > maxDecryptBlock)
				{
					cache = cipher.doFinal(encryptedData, offSet, maxDecryptBlock);
				}
				else
				{
					cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
				}
				out.write(cache, 0, cache.length);
				i++;
				offSet = i * maxDecryptBlock;
			}
			byte[] decryptedData = out.toByteArray();
			out.close();
			return decryptedData;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
    }

    /**
     * @param encryptedData 已加密数据
     * @param publicKey 公钥(BASE64编码) 由genKeyPair提供
     * @return 公钥解密后数据
     */
    public static final byte[] decryptRSAByPublicKey(byte[] encryptedData, String publicKey)
    {
        try
		{
			byte[] keyBytes = decodeBase64(publicKey);
			X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			PublicKey publicK = keyFactory.generatePublic(x509KeySpec);
			Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
			cipher.init(Cipher.DECRYPT_MODE, publicK);
			int inputLen = encryptedData.length;
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			int offSet = 0;
			byte[] cache;
			final int maxDecryptBlock = 128;
			int i = 0;
			// 对数据分段解密
			while (inputLen - offSet > 0)
			{
				if (inputLen - offSet > maxDecryptBlock)
				{
					cache = cipher.doFinal(encryptedData, offSet, maxDecryptBlock);
				}
				else
				{
					cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
				}
				out.write(cache, 0, cache.length);
				i++;
				offSet = i * maxDecryptBlock;
			}
			byte[] decryptedData = out.toByteArray();
			out.close();
			return decryptedData;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
    }

    /**
     * @param data 源数据
     * @param publicKey 公钥(BASE64编码) 由genKeyPair提供
     * @return 公钥加密后数据
     */
    public static final byte[] encryptRSAByPublicKey(byte[] data, String publicKey)
    {
        try
		{
			byte[] keyBytes = decodeBase64(publicKey);
			X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			PublicKey publicK = keyFactory.generatePublic(x509KeySpec);
			Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
			cipher.init(Cipher.ENCRYPT_MODE, publicK);
			int inputLen = data.length;
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			int offSet = 0;
			byte[] cache;
			final int maxEncryptBlock = 117;
			int i = 0;
			// 对数据分段加密
			while (inputLen - offSet > 0)
			{
				if (inputLen - offSet > maxEncryptBlock)
				{
					cache = cipher.doFinal(data, offSet, maxEncryptBlock);
				}
				else
				{
					cache = cipher.doFinal(data, offSet, inputLen - offSet);
				}
				out.write(cache, 0, cache.length);
				i++;
				offSet = i * maxEncryptBlock;
			}
			byte[] encryptedData = out.toByteArray();
			out.close();
			return encryptedData;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
    }

    /**
     * @param data 源数据
     * @param privateKey 私钥(BASE64编码) 由genKeyPair提供
     * @return 私钥加密后数据
     */
    public static final byte[] encryptRSAByPrivateKey(byte[] data, String privateKey)
    {
        try
		{
			byte[] keyBytes = decodeBase64(privateKey);
			PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			PrivateKey privateK = keyFactory.generatePrivate(pkcs8KeySpec);
			Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
			cipher.init(Cipher.ENCRYPT_MODE, privateK);
			int inputLen = data.length;
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			int offSet = 0;
			byte[] cache;
			final int maxEncryptBlock = 117;
			int i = 0;
			// 对数据分段加密
			while (inputLen - offSet > 0)
			{
				if (inputLen - offSet > maxEncryptBlock)
				{
					cache = cipher.doFinal(data, offSet, maxEncryptBlock);
				}
				else
				{
					cache = cipher.doFinal(data, offSet, inputLen - offSet);
				}
				out.write(cache, 0, cache.length);
				i++;
				offSet = i * maxEncryptBlock;
			}
			byte[] encryptedData = out.toByteArray();
			out.close();
			return encryptedData;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
    }
    
    /** 用私钥对信息生成数字签名 */  
    public static final String signDSA(byte[] data, String privateKey)
    {  
        try
		{
			byte[] keyBytes = decodeBase64(privateKey);
			PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
			KeyFactory keyFactory = KeyFactory.getInstance("DSA");
			PrivateKey priKey = keyFactory.generatePrivate(pkcs8KeySpec);
			Signature signature = Signature.getInstance(keyFactory.getAlgorithm());
			signature.initSign(priKey);
			signature.update(data);
			return encodeBase64(signature.sign());
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
    }

    /** 校验数字签名,成功返回true,失败返回false */  
    public static final boolean verifyDSA(byte[] data, String publicKey, String sign)  
    {  
        try
		{
			byte[] keyBytes = decodeBase64(publicKey);
			X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
			KeyFactory keyFactory = KeyFactory.getInstance("DSA");
			PublicKey pubKey = keyFactory.generatePublic(keySpec);
			Signature signature = Signature.getInstance(keyFactory.getAlgorithm());
			signature.initVerify(pubKey);
			signature.update(data);
			return signature.verify(decodeBase64(sign));
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}  
    }  

    /** 用于存储非对称加密的公钥私钥对 */
    public static final class KeyPairString
    {
    	public String publicKey;
    	public String privateKey;
    }
}
