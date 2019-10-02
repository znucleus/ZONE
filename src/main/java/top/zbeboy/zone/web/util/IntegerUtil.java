package top.zbeboy.zone.web.util;

import org.apache.commons.lang3.math.NumberUtils;

public class IntegerUtil {
    public static Byte toByte(int i) {
        return NumberUtils.toByte(String.valueOf(i));
    }

    public static Integer toInteger(byte b) {
        return NumberUtils.toInt(String.valueOf(b));
    }
}
