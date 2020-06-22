package top.zbeboy.zone.service.system;

import org.apache.commons.codec.CharEncoding;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import top.zbeboy.zbase.config.Workbook;
import top.zbeboy.zbase.config.ZoneProperties;
import top.zbeboy.zbase.domain.tables.pojos.SystemConfigure;
import top.zbeboy.zbase.domain.tables.pojos.SystemSmsLog;
import top.zbeboy.zone.feign.system.SystemConfigureService;
import top.zbeboy.zone.feign.system.SystemSmsLogService;
import top.zbeboy.zbase.tools.service.util.DateTimeUtil;
import top.zbeboy.zbase.tools.service.util.UUIDUtil;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Arrays;

@Service("systemMobileService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class SystemMobileServiceImpl implements SystemMobileService {

    private final Logger log = LoggerFactory.getLogger(SystemMobileServiceImpl.class);

    @Autowired
    private ZoneProperties ZoneProperties;

    @Resource
    private SystemConfigureService systemConfigureService;

    @Resource
    private SystemSmsLogService systemSmsLogService;

    @Async
    @Override
    public void sendShortMessage(String mobile, String content, String action, String sendType, String codingType, String backEncodeType) {
        String result;
        try {
            if (StringUtils.isBlank(codingType)) {
                codingType = CharEncoding.UTF_8;
            }
            if (StringUtils.isBlank(backEncodeType)) {
                backEncodeType = CharEncoding.UTF_8;
            }
            StringBuilder send = new StringBuilder();
            if (StringUtils.isNotBlank(action)) {
                send.append("action=").append(action);
            } else {
                send.append("action=send");
            }

            send.append("&userid=").append(ZoneProperties.getMobile().getUserId());
            send.append("&account=").append(
                    URLEncoder.encode(ZoneProperties.getMobile().getAccount(), codingType));
            send.append("&password=").append(
                    URLEncoder.encode(ZoneProperties.getMobile().getPassword(), codingType));
            send.append("&mobile=").append(mobile);
            send.append("&content=").append(
                    URLEncoder.encode(content, codingType));
            if (StringUtils.isNotBlank(sendType) && StringUtils.equalsIgnoreCase("get", sendType)) {
                result = SmsClientAccessTool.getInstance().doAccessHTTPGet(
                        ZoneProperties.getMobile().getUrl() + "?" + send.toString(), backEncodeType);
            } else {
                result = SmsClientAccessTool.getInstance().doAccessHTTPPost(ZoneProperties.getMobile().getUrl(),
                        send.toString(), backEncodeType);
            }
        } catch (Exception e) {
            log.error("发送短信异常:{}", e);
            result = e.getMessage();
        }

        SystemSmsLog systemSmsLog = new SystemSmsLog();
        systemSmsLog.setLogId(UUIDUtil.getUUID());
        systemSmsLog.setSendTime(DateTimeUtil.getNowSqlTimestamp());
        systemSmsLog.setSendConent(content);
        systemSmsLog.setAcceptPhone(mobile);
        systemSmsLog.setSendCondition(result);
        systemSmsLogService.save(systemSmsLog);
    }

    @Async
    @Override
    public void sendValidMobileShortMessage(String mobile, String verificationCode) {
        log.debug(" mobile valid : {} : {}", mobile, verificationCode);
        SystemConfigure systemConfigure = systemConfigureService.findByDataKey(Workbook.SystemConfigure.MOBILE_SWITCH.name());
        if (StringUtils.equals("1", systemConfigure.getDataValue())) {
            String content = "【" + ZoneProperties.getMobile().getSign() + "】 您的验证码是:" + verificationCode + "，感谢您的使用！";
            sendShortMessage(mobile, content, "", "", "", "");
        } else {
            log.debug(" 管理员已关闭短信发送 ");
        }
    }

    static class SmsClientAccessTool {

        private final Logger log = LoggerFactory.getLogger(SmsClientAccessTool.class);

        private static SmsClientAccessTool smsClientToolInstance;

        private static SmsClientAccessTool getInstance() {
            if (smsClientToolInstance == null) {
                smsClientToolInstance = new SmsClientAccessTool();
            }
            return smsClientToolInstance;
        }

        /**
         * POST方法
         *
         * @param sendUrl        ：访问URL
         * @param sendParam      ：参数串
         * @param backEncodeType ：返回的编码
         * @return 结果
         */
        String doAccessHTTPPost(String sendUrl, String sendParam,
                                String backEncodeType) {
            StringBuilder receive = new StringBuilder();
            BufferedReader rd = null;
            try {
                if (StringUtils.isBlank(backEncodeType)) {
                    backEncodeType = CharEncoding.UTF_8;
                }

                URL url = new URL(sendUrl);
                HttpURLConnection URLConn = (HttpURLConnection) url
                        .openConnection();

                URLConn.setDoOutput(true);
                URLConn.setDoInput(true);
                URLConn.setRequestMethod("POST");
                URLConn.setUseCaches(false);
                URLConn.setAllowUserInteraction(true);
                HttpURLConnection.setFollowRedirects(true);
                URLConn.setInstanceFollowRedirects(true);

                URLConn.setRequestProperty("Content-Type",
                        "application/x-www-form-urlencoded;charset=UTF-8");

                URLConn.setRequestProperty("Content-Length", Arrays.toString(sendParam.getBytes()));

                DataOutputStream dos = new DataOutputStream(URLConn
                        .getOutputStream());
                dos.writeBytes(sendParam);

                rd = new BufferedReader(new InputStreamReader(
                        URLConn.getInputStream(), backEncodeType));
                String line = rd.readLine();
                while (line != null) {
                    receive.append(line).append("\r\n");
                    line = rd.readLine();
                }
            } catch (IOException e) {
                receive.append("访问产生了异常-->").append(e.getMessage());
                log.error("Send sms error . {}", e);
            } finally {
                if (rd != null) {
                    try {
                        rd.close();
                    } catch (IOException e) {
                        log.error("Send sms error . {}", e);
                    }
                }
            }
            return receive.toString();
        }

        String doAccessHTTPGet(String sendUrl, String backEncodeType) {
            StringBuilder receive = new StringBuilder();
            BufferedReader in = null;
            try {
                if (StringUtils.isBlank(backEncodeType)) {
                    backEncodeType = CharEncoding.UTF_8;
                }

                URL url = new URL(sendUrl);
                HttpURLConnection URLConn = (HttpURLConnection) url
                        .openConnection();

                URLConn.setDoOutput(true);
                URLConn.setDoInput(true);
                URLConn.connect();
                URLConn.getOutputStream().flush();
                in = new BufferedReader(new InputStreamReader(URLConn
                        .getInputStream(), backEncodeType));

                String line = in.readLine();
                while (line != null) {
                    receive.append(line).append("\r\n");
                    line = in.readLine();
                }

            } catch (IOException e) {
                receive.append("访问产生了异常-->").append(e.getMessage());
                log.error("Send sms error . {}", e);
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException ex) {
                        log.error("Send sms error . {}", ex);
                    }
                }
            }

            return receive.toString();
        }
    }
}
