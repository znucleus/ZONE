package top.zbeboy.zone.test;

import org.junit.Test;
import top.zbeboy.zbase.tools.web.util.PinYinUtil;

public class TestPinYin {

    @Test
    public void testPinYin() {
        String name = "张三";
        System.out.println(PinYinUtil.changeToUpper(name));
    }
}
