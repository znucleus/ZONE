package top.zbeboy.zone.test;

import org.junit.Test;
import top.zbeboy.zone.service.util.UUIDUtil;

public class TestUuid {

    @Test
    public void testGetUuid() {
        System.out.println(UUIDUtil.getUUID());
    }
}
