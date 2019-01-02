import com.zk.utils.RsaUtils;
import org.junit.Test;

import java.util.Map;

public class RsaUtilsTest {

  static String privateKey = null;
  static String publicKey = null;
  private static final String UTF8 = "UTF-8";

  static {
    try {
      Map<String, String> keyPair = RsaUtils.genKeyPair();
      privateKey = keyPair.get(RsaUtils.PRIVATE_KEY);
      publicKey = keyPair.get(RsaUtils.PUBLIC_KEY);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  //公私钥生成
  @Test
  public void testGenerateKey() throws Exception {
    Map<String, String> keyPair = RsaUtils.genKeyPair();
    privateKey = keyPair.get(RsaUtils.PRIVATE_KEY);
    publicKey = keyPair.get(RsaUtils.PUBLIC_KEY);
    System.out.println("公钥：" + publicKey);
    System.out.println("私钥：" + privateKey);

  }

  @Test
  public void testSign() throws Exception {
    String src = "这是待加签文本";
    String sign = RsaUtils.sign(src, privateKey);
    System.out.println("签名：" + sign);
    System.out.println("sign = " + sign.length());
    boolean verify = RsaUtils.verify(src, publicKey, sign);
    System.out.println("验签结果：" + verify);
  }

  @Test
  public void testEncryptAndDecrypt() throws Exception {
    String src = "我是原始的业务数据，需要加密传输";
    System.out.println("原始数据：" + src);
    //加密
    String encodeEncryptStr = RsaUtils.encryptByPublicKey(src, publicKey);
    System.out.println("加密后：" + encodeEncryptStr);
    //解密
    String decryptStr = RsaUtils.decryptByPrivateKey(encodeEncryptStr, privateKey);
    System.out.println("解密后：" + decryptStr);
  }


}
