package top.zbeboy.zone.test;

import org.jasypt.encryption.StringEncryptor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestJasypt {

    @Autowired
    private StringEncryptor stringEncryptor;

    @Test
    public void encryptPwd() {
        String result1 = stringEncryptor.encrypt("root");
        System.out.println(result1);

        String result2 = stringEncryptor.encrypt("123456");
        System.out.println(result2);
    }
}
