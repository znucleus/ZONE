package top.zbeboy.zone.service.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class IPTimeStamp {
    private String ip = null;

    public IPTimeStamp() {

    }

    public IPTimeStamp(String ip) {
        this.ip = ip;
    }

    public String getIPTimeRand() {
        StringBuilder buf = new StringBuilder();
        if (this.ip != null) {
            String s[] = this.ip.split("\\.");
            for (String value : s) {
                buf.append(this.addZero(value));
            }
        }
        buf.append(this.getTimeStamp());
        Random r = new Random();
        for (int i = 0; i < 3; i++) {
            buf.append(r.nextInt(10));
        }
        return buf.toString();
    }

    private String addZero(String str) {
        StringBuilder s = new StringBuilder();
        s.append(str);
        while (s.length() < 3) {
            s.insert(0, "0");
        }
        return s.toString();
    }

    private String getTimeStamp() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
    }
}
