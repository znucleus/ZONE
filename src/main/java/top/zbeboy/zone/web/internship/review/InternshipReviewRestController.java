package top.zbeboy.zone.web.internship.review;

import org.apache.commons.lang3.StringUtils;
import org.jooq.Record;
import org.jooq.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.zbeboy.zone.config.Workbook;
import top.zbeboy.zone.domain.tables.pojos.InternshipReviewAuthorize;
import top.zbeboy.zone.domain.tables.pojos.Users;
import top.zbeboy.zone.service.internship.InternshipReleaseService;
import top.zbeboy.zone.service.internship.InternshipReviewAuthorizeService;
import top.zbeboy.zone.service.internship.InternshipReviewService;
import top.zbeboy.zone.service.platform.UsersService;
import top.zbeboy.zone.service.system.AuthoritiesService;
import top.zbeboy.zone.service.util.DateTimeUtil;
import top.zbeboy.zone.web.bean.internship.release.InternshipReleaseBean;
import top.zbeboy.zone.web.bean.internship.review.InternshipReviewAuthorizeBean;
import top.zbeboy.zone.web.internship.common.InternshipConditionCommon;
import top.zbeboy.zone.web.util.AjaxUtil;
import top.zbeboy.zone.web.util.BooleanUtil;
import top.zbeboy.zone.web.util.pagination.SimplePaginationUtil;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class InternshipReviewRestController {

    @Resource
    private InternshipConditionCommon internshipConditionCommon;

    @Resource
    private InternshipReleaseService internshipReleaseService;

    @Resource
    private InternshipReviewService internshipReviewService;

    @Resource
    private InternshipReviewAuthorizeService internshipReviewAuthorizeService;

    @Resource
    private AuthoritiesService authoritiesService;

    @Resource
    private UsersService usersService;

    /**
     * 数据
     *
     * @param simplePaginationUtil 请求
     * @return 数据
     */
    @GetMapping("/web/internship/review/internship/data")
    public ResponseEntity<Map<String, Object>> internshipData(SimplePaginationUtil simplePaginationUtil) {
        AjaxUtil<InternshipReleaseBean> ajaxUtil = AjaxUtil.of();
        List<InternshipReleaseBean> beans = new ArrayList<>();
        Result<Record> records = internshipReleaseService.findAllByPage(simplePaginationUtil);
        if (records.isNotEmpty()) {
            beans = records.into(InternshipReleaseBean.class);
            beans.forEach(bean -> bean.setTeacherDistributionStartTimeStr(DateTimeUtil.defaultFormatSqlTimestamp(bean.getTeacherDistributionStartTime())));
            beans.forEach(bean -> bean.setTeacherDistributionEndTimeStr(DateTimeUtil.defaultFormatSqlTimestamp(bean.getTeacherDistributionEndTime())));
            beans.forEach(bean -> bean.setStartTimeStr(DateTimeUtil.defaultFormatSqlTimestamp(bean.getStartTime())));
            beans.forEach(bean -> bean.setEndTimeStr(DateTimeUtil.defaultFormatSqlTimestamp(bean.getEndTime())));
            beans.forEach(bean -> bean.setReleaseTimeStr(DateTimeUtil.defaultFormatSqlTimestamp(bean.getReleaseTime())));
            beans.forEach(bean -> {
                bean.setCanOperator(BooleanUtil.toByte(internshipConditionCommon.reviewCondition(bean.getInternshipReleaseId())));
                if (BooleanUtil.toBoolean(bean.getCanOperator())) {
                    bean.setWaitTotalData(internshipReviewService.countByInternshipReleaseIdAndInternshipApplyState(bean.getInternshipReleaseId(), 1));
                    bean.setPassTotalData(internshipReviewService.countByInternshipReleaseIdAndInternshipApplyState(bean.getInternshipReleaseId(), 2));
                    bean.setFailTotalData(internshipReviewService.countByInternshipReleaseIdAndInternshipApplyState(bean.getInternshipReleaseId(), 3));
                    bean.setBasicApplyTotalData(internshipReviewService.countByInternshipReleaseIdAndInternshipApplyState(bean.getInternshipReleaseId(), 4));
                    bean.setCompanyApplyTotalData(internshipReviewService.countByInternshipReleaseIdAndInternshipApplyState(bean.getInternshipReleaseId(), 6));
                    bean.setBasicFillTotalData(internshipReviewService.countByInternshipReleaseIdAndInternshipApplyState(bean.getInternshipReleaseId(), 5));
                    bean.setCompanyFillTotalData(internshipReviewService.countByInternshipReleaseIdAndInternshipApplyState(bean.getInternshipReleaseId(), 7));
                }
            });
            beans.forEach(bean -> bean.setCanAuthorize(BooleanUtil.toByte(internshipConditionCommon.reviewAuthorizeCondition(bean.getInternshipReleaseId()))));
        }
        simplePaginationUtil.setTotalSize(internshipReleaseService.countAll(simplePaginationUtil));
        ajaxUtil.success().list(beans).page(simplePaginationUtil).msg("获取数据成功");
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 数据
     *
     * @return 数据
     */
    @GetMapping("/web/internship/review/authorize/data")
    public ResponseEntity<Map<String, Object>> authorizeData(SimplePaginationUtil simplePaginationUtil) {
        AjaxUtil<InternshipReviewAuthorizeBean> ajaxUtil = AjaxUtil.of();
        List<InternshipReviewAuthorizeBean> beans = new ArrayList<>();
        Result<Record> records = internshipReviewAuthorizeService.findAll(simplePaginationUtil);
        if (records.isNotEmpty()) {
            beans = records.into(InternshipReviewAuthorizeBean.class);
        }
        simplePaginationUtil.setTotalSize(internshipReviewAuthorizeService.countAll(simplePaginationUtil));
        ajaxUtil.success().list(beans).page(simplePaginationUtil).msg("获取数据成功");
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 保存权限
     *
     * @param internshipReleaseId 实习发布id
     * @param username            账号
     * @return true or false
     */
    @PostMapping("/web/internship/review/authorize/save")
    public ResponseEntity<Map<String, Object>> save(@RequestParam("internshipReleaseId") String internshipReleaseId,
                                                    @RequestParam("username") String username) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        String param = StringUtils.deleteWhitespace(username);
        if (internshipConditionCommon.reviewAuthorizeCondition(internshipReleaseId)) {
            // 系统或管理员不需要添加权限
            List<String> authorities = new ArrayList<>();
            authorities.add(Workbook.authorities.ROLE_SYSTEM.name());
            authorities.add(Workbook.authorities.ROLE_ADMIN.name());
            Result<Record> authorityRecord = authoritiesService.findByUsernameAndInAuthorities(param, authorities);
            if (authorityRecord.isEmpty()) {
                // 本人无需添加权限
                Users users = usersService.getUserFromSession();
                if (!StringUtils.equals(users.getUsername(), param)) {
                    Optional<Record> record = internshipReviewAuthorizeService.findByInternshipReleaseIdAndUsername(internshipReleaseId, param);
                    if (!record.isPresent()) {
                        InternshipReviewAuthorize internshipReviewAuthorize = new InternshipReviewAuthorize(internshipReleaseId, param);
                        internshipReviewAuthorizeService.save(internshipReviewAuthorize);
                        ajaxUtil.success().msg("保存成功");
                    } else {
                        ajaxUtil.fail().msg("该账号已有权限");
                    }
                } else {
                    ajaxUtil.fail().msg("本人无需添加权限");
                }
            } else {
                ajaxUtil.fail().msg("系统或管理员无需添加权限");
            }
        } else {
            ajaxUtil.fail().msg("您无权限操作");
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 删除
     *
     * @param internshipReleaseId 实习发布id
     * @param username            账号
     * @return true or false
     */
    @PostMapping("/web/internship/review/authorize/delete")
    public ResponseEntity<Map<String, Object>> delete(@RequestParam("internshipReleaseId") String internshipReleaseId,
                                                      @RequestParam("username") String username) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        if (internshipConditionCommon.reviewAuthorizeCondition(internshipReleaseId)) {
            internshipReviewAuthorizeService.deleteByInternshipReleaseIdAndUsername(internshipReleaseId, username);
            ajaxUtil.success().msg("删除成功");
        } else {
            ajaxUtil.fail().msg("您无权限操作");
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }
}
