package top.zbeboy.zone.web.platform.app;

import org.jooq.Record;
import org.jooq.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.zbeboy.zone.config.Workbook;
import top.zbeboy.zone.domain.tables.pojos.OauthAccessToken;
import top.zbeboy.zone.domain.tables.pojos.OauthClientDetails;
import top.zbeboy.zone.domain.tables.pojos.OauthClientUsers;
import top.zbeboy.zone.domain.tables.pojos.Users;
import top.zbeboy.zone.service.platform.*;
import top.zbeboy.zone.service.util.BCryptUtil;
import top.zbeboy.zone.service.util.DateTimeUtil;
import top.zbeboy.zone.web.bean.platform.app.OauthClientUsersBean;
import top.zbeboy.zone.web.util.AjaxUtil;
import top.zbeboy.zone.web.util.pagination.DataTablesUtil;
import top.zbeboy.zone.web.vo.platform.app.AppAddVo;
import top.zbeboy.zone.web.vo.platform.app.AppEditVo;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;

@RestController
public class AppRestController {

    @Resource
    private OauthClientUsersService oauthClientUsersService;

    @Resource
    private OauthClientDetailsService oauthClientDetailsService;

    @Resource
    private OauthAccessTokenService oauthAccessTokenService;

    @Resource
    private OauthRefreshTokenService oauthRefreshTokenService;

    @Resource
    private OauthApprovalsService oauthApprovalsService;

    @Resource
    private UsersService usersService;

    @Resource
    private RoleService roleService;

    /**
     * 数据
     *
     * @param request 请求
     * @return 数据
     */
    @GetMapping("/web/platform/app/data")
    public ResponseEntity<DataTablesUtil> data(HttpServletRequest request) {
        // 前台数据标题 注：要和前台标题顺序一致，获取order用
        List<String> headers = new ArrayList<>();
        headers.add("appName");
        headers.add("username");
        headers.add("realName");
        headers.add("clientId");
        headers.add("secret");
        headers.add("webServerRedirectUri");
        headers.add("remark");
        headers.add("createDate");
        headers.add("operator");
        DataTablesUtil dataTablesUtil = new DataTablesUtil(request, headers);
        Result<Record> records = oauthClientUsersService.findAllByPage(dataTablesUtil);
        List<OauthClientUsersBean> beans = new ArrayList<>();
        if (Objects.nonNull(records) && records.isNotEmpty()) {
            beans = records.into(OauthClientUsersBean.class);
            beans.forEach(bean -> bean.setCreateDateStr(DateTimeUtil.defaultFormatSqlTimestamp(bean.getCreateDate())));
        }
        dataTablesUtil.setData(beans);
        dataTablesUtil.setiTotalRecords(oauthClientUsersService.countAll(dataTablesUtil));
        dataTablesUtil.setiTotalDisplayRecords(oauthClientUsersService.countByCondition(dataTablesUtil));
        return new ResponseEntity<>(dataTablesUtil, HttpStatus.OK);
    }

    /**
     * 保存
     *
     * @param appAddVo      应用
     * @param bindingResult 检验
     * @return true 保存成功 false 保存失败
     */
    @PostMapping("/web/platform/app/save")
    public ResponseEntity<Map<String, Object>> save(@Valid AppAddVo appAddVo, BindingResult bindingResult) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        if (!bindingResult.hasErrors()) {
            OauthClientDetails oauthClientDetails = new OauthClientDetails();
            oauthClientDetails.setClientId(appAddVo.getClientId());
            oauthClientDetails.setResourceIds("product_api");
            oauthClientDetails.setClientSecret(BCryptUtil.bCryptPassword(appAddVo.getSecret()));
            oauthClientDetails.setScope("api");
            oauthClientDetails.setAuthorizedGrantTypes("authorization_code,refresh_token");
            oauthClientDetails.setWebServerRedirectUri(appAddVo.getWebServerRedirectUri());
            oauthClientDetails.setAccessTokenValidity(604800);
            oauthClientDetails.setRefreshTokenValidity(1209600);
            oauthClientDetails.setAutoapprove("false");

            oauthClientDetailsService.save(oauthClientDetails);

            OauthClientUsers oauthClientUsers = new OauthClientUsers();
            oauthClientUsers.setClientId(appAddVo.getClientId());
            oauthClientUsers.setSecret(appAddVo.getSecret());
            oauthClientUsers.setAppName(appAddVo.getAppName());

            Users users = usersService.getUserFromSession();
            oauthClientUsers.setUsername(users.getUsername());
            oauthClientUsers.setRemark(appAddVo.getRemark());
            oauthClientUsers.setCreateDate(DateTimeUtil.getNowSqlTimestamp());

            oauthClientUsersService.save(oauthClientUsers);
            ajaxUtil.success().msg("保存成功");
        } else {
            ajaxUtil.fail().msg(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 更新
     *
     * @param appEditVo     应用
     * @param bindingResult 检验
     * @return true 保存成功 false 保存失败
     */
    @PostMapping("/web/platform/app/update")
    public ResponseEntity<Map<String, Object>> update(@Valid AppEditVo appEditVo, BindingResult bindingResult) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        if (!bindingResult.hasErrors()) {
            Optional<Record> record;
            if (roleService.isCurrentUserInRole(Workbook.authorities.ROLE_SYSTEM.name())) {
                record = oauthClientUsersService.findByIdRelation(appEditVo.getClientId());
            } else {
                Users users = usersService.getUserFromSession();
                record = oauthClientUsersService.findByIdAndUsernameRelation(appEditVo.getClientId(), users.getUsername());
            }

            if (record.isPresent()) {
                OauthClientDetails oauthClientDetails = record.get().into(OauthClientDetails.class);
                oauthClientDetails.setWebServerRedirectUri(appEditVo.getWebServerRedirectUri());
                oauthClientDetailsService.update(oauthClientDetails);

                OauthClientUsers oauthClientUsers = record.get().into(OauthClientUsers.class);
                oauthClientUsers.setAppName(appEditVo.getAppName());
                oauthClientUsers.setRemark(appEditVo.getRemark());

                oauthClientUsersService.update(oauthClientUsers);

                ajaxUtil.success().msg("更新成功");
            } else {
                ajaxUtil.fail().msg("根据ID未查询到应用数据");
            }
        } else {
            ajaxUtil.fail().msg(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 根据客户端id删除
     *
     * @param clientId 客户端id
     * @return true or false
     */
    @PostMapping("/web/platform/app/delete")
    public ResponseEntity<Map<String, Object>> delete(@RequestParam("clientId") String clientId) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        Optional<Record> record;
        if (roleService.isCurrentUserInRole(Workbook.authorities.ROLE_SYSTEM.name())) {
            record = oauthClientUsersService.findByIdRelation(clientId);
        } else {
            Users users = usersService.getUserFromSession();
            record = oauthClientUsersService.findByIdAndUsernameRelation(clientId, users.getUsername());
        }

        if (record.isPresent()) {
            List<OauthAccessToken> oauthAccessTokens = oauthAccessTokenService.findByClientId(clientId);
            if (Objects.nonNull(oauthAccessTokens)) {
                oauthAccessTokens.forEach(oauthAccessToken -> oauthRefreshTokenService.deleteByTokenId(oauthAccessToken.getRefreshToken()));
            }
            oauthAccessTokenService.deleteByClientId(clientId);
            oauthApprovalsService.deleteByClientId(clientId);
            oauthClientDetailsService.deleteById(clientId);

            OauthClientUsers oauthClientUsers = record.get().into(OauthClientUsers.class);
            oauthClientUsersService.delete(oauthClientUsers);

            ajaxUtil.success().msg("删除成功");
        } else {
            ajaxUtil.fail().msg("根据ID未查询到应用数据");
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 备注
     *
     * @param clientId id
     * @param remark   数据
     * @return 备注
     */
    @PostMapping("/web/platform/app/remark")
    public ResponseEntity<Map<String, Object>> remark(@RequestParam("clientId") String clientId, String remark) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        Optional<Record> record;
        if (roleService.isCurrentUserInRole(Workbook.authorities.ROLE_SYSTEM.name())) {
            record = oauthClientUsersService.findByIdRelation(clientId);
        } else {
            Users users = usersService.getUserFromSession();
            record = oauthClientUsersService.findByIdAndUsernameRelation(clientId, users.getUsername());
        }

        if (record.isPresent()) {
            OauthClientUsers oauthClientUsers = record.get().into(OauthClientUsers.class);
            oauthClientUsers.setRemark(remark);
            oauthClientUsersService.update(oauthClientUsers);

            ajaxUtil.success().msg("备注成功");
        } else {
            ajaxUtil.fail().msg("根据ID未查询到应用数据");
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }
}
