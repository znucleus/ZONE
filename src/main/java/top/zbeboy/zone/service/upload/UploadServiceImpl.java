package top.zbeboy.zone.service.upload;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import top.zbeboy.zone.service.util.IPTimeStamp;
import top.zbeboy.zone.service.util.RequestUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

@Service("uploadService")
public class UploadServiceImpl implements UploadService {

    private final Logger log = LoggerFactory.getLogger(UploadServiceImpl.class);

    @Override
    public List<FileBean> upload(MultipartHttpServletRequest request, String path, String address) {
        List<FileBean> list = new ArrayList<>();
        //1. build an iterator.
        Iterator<String> iterator = request.getFileNames();
        MultipartFile multipartFile;
        //2. get each file
        while (iterator.hasNext()) {
            FileBean fileBean = new FileBean();
            //2.1 get next MultipartFile
            multipartFile = request.getFile(iterator.next());
            assert multipartFile != null;
            log.info(multipartFile.getOriginalFilename() + " uploaded!");
            fileBean.setContentType(multipartFile.getContentType());
            IPTimeStamp ipTimeStamp = new IPTimeStamp(address);
            String[] words = Objects.requireNonNull(multipartFile.getOriginalFilename()).split("\\.");
            if (words.length > 1) {
                String ext = words[words.length - 1].toLowerCase();
                String filename = ipTimeStamp.getIPTimeRand() + "." + ext;
                if (filename.contains(":")) {
                    filename = filename.substring(filename.lastIndexOf(':') + 1);
                }
                fileBean.setOriginalFileName(multipartFile.getOriginalFilename().substring(0, multipartFile.getOriginalFilename().lastIndexOf('.')));
                fileBean.setExt(ext);
                fileBean.setNewName(filename);
                fileBean.setSize(multipartFile.getSize());
                //copy file to local disk (make sure the path "e.g. D:/temp/files" exists)
                buildList(fileBean, list, path, filename, multipartFile);
            } else {
                // no filename
                String filename = ipTimeStamp.getIPTimeRand();
                fileBean.setOriginalFileName(multipartFile.getOriginalFilename().substring(0, multipartFile.getOriginalFilename().lastIndexOf('.')));
                fileBean.setNewName(filename);
                fileBean.setSize(multipartFile.getSize());
                // copy file to local disk (make sure the path "e.g. D:/temp/files" exists)
                buildList(fileBean, list, path, filename, multipartFile);
            }
        }
        return list;
    }

    private String buildPath(String path, String filename, MultipartFile multipartFile) throws IOException {
        String lastPath = "";
        File saveFile = new File(path, filename);
        log.info(path);
        if (multipartFile.getSize() < new File(path.split(":")[0] + ":").getFreeSpace()) {// has space with disk
            if (!saveFile.getParentFile().exists()) {//create file
                saveFile.getParentFile().mkdirs();
            }
            log.info(path);
            FileCopyUtils.copy(multipartFile.getBytes(), new FileOutputStream(path + File.separator + filename));
            lastPath = path + File.separator + filename;
            lastPath = lastPath.replaceAll("\\\\", "/");
        } else {
            log.info("Not valiablespace!");
        }
        return lastPath;
    }

    private void buildList(FileBean fileBean, List<FileBean> list, String path, String filename, MultipartFile multipartFile) {
        try {
            if (!StringUtils.isEmpty(path.split(":")[0])) {
                fileBean.setLastPath(buildPath(path, filename, multipartFile));
                list.add(fileBean);
            }
        } catch (IOException e) {
            log.error("Build File list exception : ", e);
        }
    }

    @Override
    public void download(String fileName, String filePath, HttpServletResponse response, HttpServletRequest request) {
        try {
            response.setContentType("application/x-msdownload");
            response.setHeader("Content-disposition", "attachment; filename=\"" + new String((fileName + filePath.substring(filePath.lastIndexOf('.'))).getBytes("gb2312"), "ISO8859-1") + "\"");
            String realPath = RequestUtil.getRealPath(request);
            InputStream inputStream = new FileInputStream(realPath + filePath);
            FileCopyUtils.copy(inputStream, response.getOutputStream());
        } catch (Exception e) {
            log.error("File is not found exception : {} ", e);
        }
    }

    @Override
    public void download(String fileName, File file, HttpServletResponse response, HttpServletRequest request) {
        try {
            String fileFullName = completionFileName(fileName, file.getName());
            response.setContentType("application/x-msdownload");
            response.setHeader("Content-disposition", "attachment; filename=\"" + new String((fileFullName).getBytes("gb2312"), "ISO8859-1") + "\"");
            FileInputStream inputStream = new FileInputStream(file);
            FileCopyUtils.copy(inputStream, response.getOutputStream());
        } catch (Exception e) {
            log.error("File is not found exception : {} ", e);
        }
    }

    /**
     * 文件名加后缀补全操作
     *
     * @param fileName 无后缀的文件名
     * @param fileFullName 文件名全称(带后缀或不带后缀)
     * @return 带后缀的文件名
     */
    private String completionFileName(String fileName, String fileFullName) {
        String tempName = fileName;
        int extLocal = fileFullName.lastIndexOf('.');
        if (extLocal > 0) {
            tempName += fileFullName.substring(extLocal);
        }
        return tempName;
    }
}
