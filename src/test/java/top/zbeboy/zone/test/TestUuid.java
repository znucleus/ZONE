package top.zbeboy.zone.test;

import org.junit.jupiter.api.Test;
import top.zbeboy.zbase.tools.service.util.UUIDUtil;

public class TestUuid {

    @Test
    public void testGetUuid() {
        System.out.println(UUIDUtil.getUUID());
    }
}
