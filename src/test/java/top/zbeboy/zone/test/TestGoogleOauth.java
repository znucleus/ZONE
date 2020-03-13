package top.zbeboy.zone.test;

import org.junit.Test;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;

public class TestGoogleOauth {

    @Test
    public void testCreateKey() {
        GoogleAuthenticator gAuth = new GoogleAuthenticator();
        final GoogleAuthenticatorKey key = gAuth.createCredentials();
        System.out.println(key.getKey());
    }

    @Test
    public void testValidCode() {
        GoogleAuthenticator gAuth = new GoogleAuthenticator();
        boolean isCodeValid = gAuth.authorize("H5FUILELYOHOR7EY", 590933);
        System.out.println(isCodeValid);
    }
}
