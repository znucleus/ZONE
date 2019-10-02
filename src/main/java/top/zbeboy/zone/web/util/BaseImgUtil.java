package top.zbeboy.zone.web.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import top.zbeboy.zone.config.Workbook;
import top.zbeboy.zone.domain.tables.pojos.Files;
import top.zbeboy.zone.service.util.IPTimeStamp;
import top.zbeboy.zone.service.util.RequestUtil;
import top.zbeboy.zone.service.util.UUIDUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.Base64;
import java.util.Objects;

public class BaseImgUtil {
    /**
     * @param imgStr   base64编码字符串
     * @param fileName file name include ext.
     * @param path     图片路径-具体到文件
     * @return files object.
     */
    public static Files generateImage(String imgStr, String fileName, HttpServletRequest request, String path, String address) throws IOException {
        assert StringUtils.isNotBlank(imgStr);
        assert StringUtils.isNotBlank(fileName) && fileName.contains(".");
        assert StringUtils.isNotBlank(path);

        String ext = fileName.substring(fileName.lastIndexOf('.') + 1);
        String extUpperCase = ext.toUpperCase();

        assert StringUtils.equals(extUpperCase, Workbook.imageSuffix.JPG.name()) ||
                StringUtils.equals(extUpperCase, Workbook.imageSuffix.JPEG.name()) ||
                StringUtils.equals(extUpperCase, Workbook.imageSuffix.BMP.name()) ||
                StringUtils.equals(extUpperCase, Workbook.imageSuffix.GIF.name()) ||
                StringUtils.equals(extUpperCase, Workbook.imageSuffix.PNG.name());

        Files files = new Files();
        files.setFileId(UUIDUtil.getUUID());
        files.setExt(ext);
        files.setOriginalFileName(fileName.substring(0, fileName.lastIndexOf('.')));

        if (StringUtils.equals(extUpperCase, Workbook.imageSuffix.JPG.name()) || StringUtils.equals(extUpperCase, Workbook.imageSuffix.JPEG.name())) {
            files.setContentType(MediaType.IMAGE_JPEG_VALUE);
        } else if (StringUtils.equals(extUpperCase, Workbook.imageSuffix.GIF.name())) {
            files.setContentType(MediaType.IMAGE_GIF_VALUE);
        } else if (StringUtils.equals(extUpperCase, Workbook.imageSuffix.PNG.name())) {
            files.setContentType(MediaType.IMAGE_PNG_VALUE);
        }

        IPTimeStamp ipTimeStamp = new IPTimeStamp(address);

        String fileTempName = ipTimeStamp.getIPTimeRand() + "." + ext;
        if (fileTempName.contains(":")) {
            fileTempName = fileTempName.substring(fileTempName.lastIndexOf(':') + 1);
        }

        files.setNewName(fileTempName.substring(0, fileTempName.lastIndexOf('.')));
        String filePath = RequestUtil.getRealPath(request) + path + fileTempName;
        files.setRelativePath(path + fileTempName);

        File file = new File(filePath);
        if (!file.getParentFile().exists()) {//create file, linux need first create path
            file.getParentFile().mkdirs();
        }

        try (OutputStream out = new FileOutputStream(file)) {
            //解密
            Base64.Decoder decoder = Base64.getDecoder();
            byte[] b = decoder.decode(imgStr.substring(imgStr.indexOf(",") + 1));
            //处理数据
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {
                    b[i] += 256;
                }
            }
            out.write(b);
        } catch (IOException e) {
            throw e;
        }


        files.setFileSize(file.length());

        return files;
    }

    /**
     * 根据图片地址转换为base64编码字符串
     *
     * @return imgstr
     */
    public static String getImageStr(String imgFile) {
        Base64.Encoder encoder = Base64.getEncoder();
        byte[] data = null;
        try (InputStream inputStream = new FileInputStream(imgFile)) {
            data = new byte[inputStream.available()];
            inputStream.read(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return encoder.encodeToString(Objects.requireNonNull(data));
    }

   /* public static void main(String[] args) {
        String strImg = getImageStr("/home/zhaoyin/图片/1.jpg");
        System.out.println(strImg);
//        generateImage(strImg, "F:/86619-107.jpg");
    }*/
}
