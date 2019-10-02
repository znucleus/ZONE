package top.zbeboy.zone.service.upload;

import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.List;

public interface UploadService {
    /**
     * 上传文件
     *
     * @param request 请求对象
     * @param path    根路径
     * @param address 地址
     * @return file data info.
     */
    List<FileBean> upload(MultipartHttpServletRequest request, String path, String address);

    /**
     * 文件下载
     *
     * @param fileName 文件名
     * @param filePath 文件路径
     * @param response 响应对象
     * @param request  请求对象
     */
    void download(String fileName, String filePath, HttpServletResponse response, HttpServletRequest request);

    /**
     * 文件下载
     *
     * @param fileName 文件名
     * @param file     文件对象
     * @param response 响应对象
     * @param request  请求对象
     */
    void download(String fileName, File file, HttpServletResponse response, HttpServletRequest request);
}
