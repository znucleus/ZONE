package top.zbeboy.zone.test;

import org.junit.jupiter.api.Test;
import top.zbeboy.zone.service.util.BCryptUtil;

public class TestOauth {

    @Test
    public void testGetSecret() {
        String password = "ukdw89weEv";
        System.out.println("{bcrypt}" + BCryptUtil.bCryptPassword(password));
    }
}
