package top.zbeboy.zone.web.util;

import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;

public class GoogleOauthUtil {

    /**
     * 创建key
     *
     * @return key
     */
    public static String createKey() {
        GoogleAuthenticator gAuth = new GoogleAuthenticator();
        final GoogleAuthenticatorKey key = gAuth.createCredentials();
        return key.getKey();
    }

    /**
     * 验证code
     *
     * @param secret key
     * @param code   code
     * @return true or false
     */
    public static boolean validCode(String secret, int code) {
        GoogleAuthenticator gAuth = new GoogleAuthenticator();
        return gAuth.authorize(secret, code);
    }
}
