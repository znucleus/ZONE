package top.zbeboy.zone.test;

import org.junit.jupiter.api.Test;

public class TestIdCard {

    @Test
    public void testIdCard() {
        int keySize = 30;
        for (int i = 1; i <= 1000; i++) {
            System.out.println(i % keySize);
        }
    }
}
