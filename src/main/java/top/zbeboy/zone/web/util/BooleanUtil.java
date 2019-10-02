package top.zbeboy.zone.web.util;

public class BooleanUtil {

    public static Byte toByte(boolean b) {
        byte bs = 0;
        if (b) {
            bs = 1;
        }
        return bs;
    }

    public static Integer toInteger(boolean b) {
        int bs = 0;
        if (b) {
            bs = 1;
        }
        return bs;
    }

    public static Boolean toBoolean(byte b) {
        boolean bs = false;
        if (b == 1) {
            bs = true;
        }
        return bs;
    }
}
