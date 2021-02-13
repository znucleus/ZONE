package top.zbeboy.zone.web.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import top.zbeboy.zbase.config.Workbook;
import top.zbeboy.zbase.domain.tables.pojos.Files;
import top.zbeboy.zbase.tools.service.util.IPTimeStamp;
import top.zbeboy.zbase.tools.service.util.RequestUtil;
import top.zbeboy.zbase.tools.service.util.UUIDUtil;
import top.zbeboy.zbase.tools.web.util.ImageUtil;

import javax.servlet.http.HttpServletRequest;
import java.awt.*;
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
    public static Files generateImage(String imgStr, String fileName, HttpServletRequest request, String path) throws IOException {
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

        IPTimeStamp ipTimeStamp = new IPTimeStamp(RequestUtil.getIpAddress(request));

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
        }


        files.setFileSize(file.length());

        return files;
    }

    /**
     * 转换成图片，并且尺寸调整，压缩
     *
     * @param imgStr    base64编码字符串
     * @param fileName  file name include ext.
     * @param path      图片路径-具体到文件
     * @param maxWidth  最大宽度
     * @param maxHeight 最大高度
     * @param quality   压缩率
     * @return files object.
     */
    public static Files generateImage(String imgStr, String fileName, HttpServletRequest request, String path, int maxWidth, int maxHeight, float quality) throws Exception {
        Files files = generateImage(imgStr, fileName, request, path);
        optimizeImage(files, request, path, maxWidth, maxHeight, quality);
        return files;
    }

    /**
     * 压缩图片
     *
     * @param files     文件
     * @param request   请求
     * @param path      文件前路径
     * @param maxWidth  最大宽度
     * @param maxHeight 最大高度
     * @param quality   压缩率
     * @throws Exception 异常
     */
    public static void optimizeImage(Files files, HttpServletRequest request, String path, int maxWidth, int maxHeight, float quality) throws Exception {
        if (StringUtils.equals(files.getExt().toUpperCase(), Workbook.imageSuffix.GIF.name())) {
            return;
        }
        String srcFilePath = RequestUtil.getRealPath(request) + files.getRelativePath();
        String resizeFilePath = RequestUtil.getRealPath(request) + path + files.getNewName() + "_resize." + files.getExt();

        File srcFile = new File(srcFilePath);
        File resizeFile = new File(resizeFilePath);
        // 尺寸变小
        int[] fileSizes = ImageUtil.getSizeInfo(srcFile);
        if (fileSizes.length > 1) {
            int width = fileSizes[0];
            int height = fileSizes[1];
            if (width > maxWidth && height > maxHeight) {
                ImageUtil.resize(srcFilePath, resizeFilePath, -1, -1, maxWidth, maxHeight);
            } else if (width > maxWidth) {
                ImageUtil.resize(srcFilePath, resizeFilePath, maxWidth, -1);
            } else if (height > maxHeight) {
                ImageUtil.resize(srcFilePath, resizeFilePath, -1, maxHeight);
            }

            if (resizeFile.exists()) {
                if (srcFile.exists() && srcFile.delete()) {
                    resizeFile.renameTo(srcFile);
                }
            }
        }

        String compressFilePath = RequestUtil.getRealPath(request) + path + files.getNewName() + "_compress." + files.getExt();
        File compressFile = new File(compressFilePath);
        if (resizeFile.exists()) {
            ImageUtil.optimize(resizeFile, compressFile, quality);
            if (compressFile.exists()) {
                if (resizeFile.delete()) {
                    compressFile.renameTo(resizeFile);
                }
            }
        } else {
            if (srcFile.exists()) {
                ImageUtil.optimize(srcFile, compressFile, quality);
                if (compressFile.exists()) {
                    if (srcFile.delete()) {
                        compressFile.renameTo(srcFile);
                    }
                }
            }
        }
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

    /*public static void main(String[] args) {
        String strImg = getImageStr("/home/zhaoyin/图片/1.jpg");
        System.out.println(strImg);
        generateImage(strImg, "F:/86619-107.jpg");
    }*/
}
