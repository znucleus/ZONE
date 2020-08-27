package top.zbeboy.zone.test;

import org.junit.Test;

public class TestIdCard {

    @Test
    public void testIdCard() {
        String idCard = "530181199401073015";
        System.out.println(idCard.substring(6, 10) + "-" + idCard.substring(10, 12) + "-" + idCard.substring(12, 14));
        System.out.println(idCard.substring(16, 17));
    }
}
