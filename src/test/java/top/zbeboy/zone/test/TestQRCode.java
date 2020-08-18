package top.zbeboy.zone.test;

import org.junit.Test;
import top.zbeboy.zbase.tools.web.util.QRCodeUtil;

public class TestQRCode {

    @Test
    public void testGenerator() throws Exception {
        // 存放在二维码中的内容
        String text = "http://127.0.0.1/anyone/campus/roster/add/121211212";
        // 嵌入二维码的图片路径
        String imgPath = "E:/qrcode/logo.png";
        // 生成的二维码的路径及名称
        String destPath = "E:/qrcode/test.jpg";
        //生成二维码
        QRCodeUtil.encode(text, imgPath, destPath, true);
        // 解析二维码
        String str = QRCodeUtil.decode(destPath);
        // 打印出解析出的内容
        System.out.println(str);
    }

    @Test
    public void testDecode() throws Exception {
        // 生成的二维码的路径及名称
        String destPath = "D:\\project\\code\\IntellijPro\\ZONE\\root\\goods\\campus\\roster\\qr_code_c00a493029cd4c8f98cfc17c43b4c81a.jpg";
        // 解析二维码
        String str = QRCodeUtil.decode(destPath);
        // 打印出解析出的内容
        System.out.println(str);
    }
}
