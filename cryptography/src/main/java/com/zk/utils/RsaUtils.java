package com.zk.utils;

import com.zk.exception.DecryptionException;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

public class RsaUtils {

  /**
   * 加密算法RSA.
   */
  public static final String KEY_ALGORITHM = "RSA";

  /**
   * 签名算法.
   */
  public static final String SIGNATURE_ALGORITHM = "SHA1WithRSA";

  /**
   * 获取公钥的key.
   */
  public static final String PUBLIC_KEY = "publicKey";

  /**
   * 获取私钥的key.
   */
  public static final String PRIVATE_KEY = "privateKey";

  /**
   * RSA最大加密明文大小.
   */
  private static final int MAX_ENCRYPT_BLOCK = 245;

  /**
   * RSA最大解密密文大小.
   */
  private static final int MAX_DECRYPT_BLOCK = 256;

  /**
   * RSA最大解密密文大小.
   */
  private static final int KEY_SIZE = 2048;

  private static final String UTF8 = "UTF-8";

  //签名.
  public static String sign(String data, String privateKey) throws Exception {
    byte[] signBytes = sign(data.getBytes(UTF8), privateKey);
    return Base64Utils.encode(signBytes);
  }

  /**
   * 签名.
   */
  private static byte[] sign(byte[] data, String privateKey) throws Exception {
    byte[] keyBytes = Base64Utils.decode(privateKey);
    PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
    KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
    PrivateKey privateK = keyFactory.generatePrivate(pkcs8KeySpec);
    Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
    signature.initSign(privateK);
    signature.update(data);
    return signature.sign();
  }

  //验签
  public static boolean verify(String data, String publicKey, String sign) throws Exception {
    return verify(data.getBytes(UTF8), publicKey, Base64Utils.decode(sign));
  }

  /**
   * 验签.
   */
  private static boolean verify(byte[] data, String publicKey, byte[] sign)
      throws Exception {
    byte[] keyBytes = Base64Utils.decode(publicKey);
    X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
    KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
    PublicKey publicK = keyFactory.generatePublic(keySpec);
    Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
    signature.initVerify(publicK);
    signature.update(data);
    return signature.verify(sign);
  }

  //私钥加密
  public static String decryptByPrivateKey(String encryptedData, String privateKey)
      throws Exception {
    byte[] decryptByte = decryptByPrivateKey(Base64Utils.decode(encryptedData), privateKey);
    return new String(decryptByte, Charset.forName(UTF8));
  }

  /**
   * 私钥加密.
   */
  private static byte[] decryptByPrivateKey(byte[] encryptedData, String privateKey) {
    try {
      byte[] keyBytes = Base64Utils.decode(privateKey);
      PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
      KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
      Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
      Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
      cipher.init(Cipher.DECRYPT_MODE, privateK);
      int inputLen = encryptedData.length;
      ByteArrayOutputStream out = new ByteArrayOutputStream();
      int offSet = 0;
      byte[] cache;
      int i = 0;
      // 对数据分段解密
      while (inputLen - offSet > 0) {
        if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
          cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
        } else {
          cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
        }
        out.write(cache, 0, cache.length);
        i++;
        offSet = i * MAX_DECRYPT_BLOCK;
      }
      byte[] decryptedData = out.toByteArray();
      out.close();
      return decryptedData;
    } catch (Exception e) {
      throw new DecryptionException("RSA解密异常", e);
    }
  }

  //公钥解密
  public static String encryptByPublicKey(String data, String publicKey) throws Exception {
    byte[] encryptBytes = encryptByPublicKey(data.getBytes(UTF8), publicKey);
    return Base64Utils.encode(encryptBytes);
  }


  /**
   * 公钥解密.
   */
  private static byte[] encryptByPublicKey(byte[] data, String publicKey)
      throws Exception {
    byte[] keyBytes = Base64Utils.decode(publicKey);
    X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
    KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
    Key publicK = keyFactory.generatePublic(x509KeySpec);
    // 对数据加密
    Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
    cipher.init(Cipher.ENCRYPT_MODE, publicK);
    int inputLen = data.length;
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    int offSet = 0;
    byte[] cache;
    int i = 0;
    // 对数据分段加密
    while (inputLen - offSet > 0) {
      if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
        cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
      } else {
        cache = cipher.doFinal(data, offSet, inputLen - offSet);
      }
      out.write(cache, 0, cache.length);
      i++;
      offSet = i * MAX_ENCRYPT_BLOCK;
    }
    byte[] encryptedData = out.toByteArray();
    out.close();
    return encryptedData;
  }

  /**
   * 获取公私钥.
   */
  public static Map<String, String> genKeyPair() throws Exception {
    KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
    keyPairGen.initialize(KEY_SIZE);
    KeyPair keyPair = keyPairGen.generateKeyPair();
    RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
    RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
    Map<String, String> keyMap = new HashMap<String, String>(2);
    keyMap.put(PUBLIC_KEY, Base64Utils.encode(publicKey.getEncoded()));
    keyMap.put(PRIVATE_KEY, Base64Utils.encode(privateKey.getEncoded()));
    return keyMap;
  }

}
