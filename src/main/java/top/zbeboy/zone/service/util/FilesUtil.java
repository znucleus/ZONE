package top.zbeboy.zone.service.util;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipMethod;
import org.springframework.util.ObjectUtils;

import java.io.File;
import java.util.List;
import java.util.Objects;

public class FilesUtil {
    /**
     * 删除硬盘上的文件
     *
     * @param path 文件路径
     */
    public static void deleteFile(String path) {
        if (!Objects.isNull(path) && !Objects.equals("", path.trim())) {
            File file = new File(path);
            if (file.exists()) {
                file.delete();
            }
        }
    }

    /**
     * 转换文件单位
     *
     * @param size 文件大小
     * @return 文件尺寸
     */
    public static String transformationFileUnit(long size) {
        String str;
        if (size < 1024) {
            str = size + "B";
        } else if (size < 1024 * 1024) {
            str = (size / 1024) + "KB";
        } else if (size < 1024 * 1024 * 1024) {
            str = (size / (1024 * 1024)) + "MB";
        } else {
            str = (size / (1024 * 1024 * 1024)) + "GB";
        }

        return str;
    }

    /**
     * 单个文件压缩成zip
     *
     * @param fileName 文件名 带后缀
     * @param zipPath  输出zip路径
     * @param filePath 文件路径，带文件名 + 后缀
     * @return 是否成功
     * @throws Exception
     */
    public static boolean compressZip(String fileName, String zipPath, String filePath) throws Exception {
        File file = new File(filePath);
        if (file.exists()) {
            ScatterSample scatterSample = new ScatterSample();
            File zipFile = new File(zipPath);
            if (!zipFile.getParentFile().exists()) {//create file
                zipFile.getParentFile().mkdirs();
            }
            ZipArchiveOutputStream zipArchiveOutputStream = new ZipArchiveOutputStream(zipFile);
            ZipArchiveEntry entry = new ZipArchiveEntry(fileName);
            entry.setMethod(ZipMethod.STORED.getCode());
            scatterSample.addEntry(entry, new ZipInputStream(file));
            scatterSample.writeTo(zipArchiveOutputStream);
            zipArchiveOutputStream.close();
            return true;
        }
        return false;
    }

    /**
     * 多个文件压缩成zip
     *
     * @param fileName 文件名 带后缀
     * @param zipPath  输出zip路径
     * @param filePath 文件路径，带文件名 + 后缀
     * @throws Exception
     */
    public static void compressZipMulti(List<String> fileName, String zipPath, List<String> filePath) throws Exception {
        File zipFile = new File(zipPath);
        if (!zipFile.getParentFile().exists()) {//create file
            zipFile.getParentFile().mkdirs();
        }
        ZipArchiveOutputStream zipArchiveOutputStream = new ZipArchiveOutputStream(zipFile);
        if (!ObjectUtils.isEmpty(fileName) && !ObjectUtils.isEmpty(filePath) && fileName.size() == filePath.size()) {
            for (int i = 0; i < fileName.size(); i++) {
                File file = new File(filePath.get(i));
                if (file.exists()) {
                    ScatterSample scatterSample = new ScatterSample();
                    ZipArchiveEntry entry = new ZipArchiveEntry(fileName.get(i));
                    entry.setMethod(ZipMethod.STORED.getCode());
                    scatterSample.addEntry(entry, new ZipInputStream(file));
                    scatterSample.writeTo(zipArchiveOutputStream);
                }
            }
        }
        zipArchiveOutputStream.close();
    }
}
