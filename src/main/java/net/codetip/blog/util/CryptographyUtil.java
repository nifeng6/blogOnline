package net.codetip.blog.util;

import org.apache.shiro.crypto.hash.Md5Hash;

/**
 * @author Administrator
 *
 *123456 =ba61ce8fa1e3725876e6363c76043c8d
 */
public class CryptographyUtil {

    public static void main(String[] args) throws Exception {
        //用于测试加密程序
        System.out.println(md5("123456", "codetip"));

    }

    /**
     * Md5加密  加盐
     *
     *            加密的内容
     * @param salt
     *            盐值
     */

    //这个方法主要是为了在我们将密码传入数据库时进行加密使用
    public static String md5(String pwd, String salt) {
        return new Md5Hash(pwd, salt).toString();
    }


}
