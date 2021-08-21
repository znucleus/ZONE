package top.zbeboy.zone.api.educational.examination;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.zbeboy.zbase.bean.educational.examination.ExaminationNoticeDetailBean;
import top.zbeboy.zbase.bean.educational.examination.ExaminationNoticeReleaseBean;
import top.zbeboy.zbase.config.Workbook;
import top.zbeboy.zbase.domain.tables.pojos.Users;
import top.zbeboy.zbase.feign.data.WeiXinSubscribeService;
import top.zbeboy.zbase.feign.educational.examination.EducationalExaminationService;
import top.zbeboy.zbase.tools.web.util.AjaxUtil;
import top.zbeboy.zbase.tools.web.util.pagination.SimplePaginationUtil;
import top.zbeboy.zbase.vo.data.weixin.WeiXinSubscribeAddVo;
import top.zbeboy.zone.annotation.logging.ApiLoggingRecord;
import top.zbeboy.zone.web.util.SessionUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Map;

@RestController
public class ExaminationApiController {
    @Resource
    private EducationalExaminationService educationalExaminationService;

    @Resource
    private WeiXinSubscribeService weiXinSubscribeService;

    /**
     * 数据
     *
     * @param simplePaginationUtil 请求
     * @return 数据
     */
    @ApiLoggingRecord(remark = "教务考试数据", channel = Workbook.channel.API, needLogin = true)
    @GetMapping("/api/educational/examination/paging")
    public ResponseEntity<Map<String, Object>> data(SimplePaginationUtil simplePaginationUtil, HttpServletRequest request, Principal principal) {
        Users users = SessionUtil.getUserFromOauth(principal);
        simplePaginationUtil.setUsername(users.getUsername());
        AjaxUtil<ExaminationNoticeReleaseBean> ajaxUtil = educationalExaminationService.data(simplePaginationUtil);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 详情数据
     *
     * @param simplePaginationUtil 请求
     * @return 数据
     */
    @ApiLoggingRecord(remark = "教务考试详情数据", channel = Workbook.channel.API, needLogin = true)
    @GetMapping("/api/educational/examination/detail/paging")
    public ResponseEntity<Map<String, Object>> detailData(SimplePaginationUtil simplePaginationUtil, Principal principal, HttpServletRequest request) {
        Users users = SessionUtil.getUserFromOauth(principal);
        simplePaginationUtil.setUsername(users.getUsername());
        AjaxUtil<ExaminationNoticeDetailBean> ajaxUtil = educationalExaminationService.detailData(simplePaginationUtil);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 短信订阅
     *
     * @param id 详情id
     * @return true or false
     */
    @ApiLoggingRecord(remark = "教务考试短信订阅数据", channel = Workbook.channel.API, needLogin = true)
    @PostMapping("/api/educational/examination/sms-subscribe/save")
    public ResponseEntity<Map<String, Object>> smsSubscribe(@RequestParam("id") String id, Principal principal, HttpServletRequest request) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        Users users = SessionUtil.getUserFromOauth(principal);
        if (StringUtils.isNotBlank(users.getMobile())) {
            ajaxUtil = educationalExaminationService.examinationSmsSubscribeSave(id, users.getMobile());
        } else {
            ajaxUtil.fail().msg("手机号为空，无法订阅");
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 微信订阅
     *
     * @param weiXinSubscribeAddVo 订阅信息
     * @return true or false
     */
    @ApiLoggingRecord(remark = "教务考试微信订阅数据", channel = Workbook.channel.API, needLogin = true)
    @PostMapping("/api/educational/examination/wei-xin-subscribe/save")
    public ResponseEntity<Map<String, Object>> weiXinSubscribe(WeiXinSubscribeAddVo weiXinSubscribeAddVo, Principal principal, HttpServletRequest request) {
        Users users = SessionUtil.getUserFromOauth(principal);
        weiXinSubscribeAddVo.setUsername(users.getUsername());
        AjaxUtil<Map<String, Object>> ajaxUtil = educationalExaminationService.examinationWeiXinSubscribeSave(weiXinSubscribeAddVo.getParamId(), weiXinSubscribeAddVo.getUsername());
        if (ajaxUtil.getState()) {
            ajaxUtil = weiXinSubscribeService.subscribe(weiXinSubscribeAddVo);
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }
}
