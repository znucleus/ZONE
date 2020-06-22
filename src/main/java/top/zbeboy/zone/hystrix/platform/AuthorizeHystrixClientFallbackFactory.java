package top.zbeboy.zone.hystrix.platform;

import org.springframework.stereotype.Component;
import top.zbeboy.zbase.domain.tables.pojos.Authorities;
import top.zbeboy.zbase.domain.tables.pojos.AuthorizeType;
import top.zbeboy.zbase.domain.tables.pojos.Role;
import top.zbeboy.zbase.domain.tables.pojos.RoleApply;
import top.zbeboy.zone.feign.platform.AuthorizeService;
import top.zbeboy.zone.web.bean.platform.authorize.RoleApplyBean;
import top.zbeboy.zbase.tools.web.util.AjaxUtil;
import top.zbeboy.zbase.tools.web.util.pagination.DataTablesUtil;
import top.zbeboy.zbase.vo.platform.authorize.AuthorizeAddVo;
import top.zbeboy.zbase.vo.platform.authorize.AuthorizeEditVo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class AuthorizeHystrixClientFallbackFactory implements AuthorizeService {
    @Override
    public RoleApply findRoleApplyById(String id) {
        return new RoleApply();
    }

    @Override
    public RoleApplyBean findRoleApplyByIdRelation(String id) {
        return new RoleApplyBean();
    }

    @Override
    public List<Authorities> findByUsername(String username) {
        return new ArrayList<>();
    }

    @Override
    public List<Authorities> findByUsernameAndInAuthorities(String username, List<String> authorities) {
        return new ArrayList<>();
    }

    @Override
    public DataTablesUtil data(DataTablesUtil dataTablesUtil) {
        dataTablesUtil.setData(new ArrayList<>());
        return dataTablesUtil;
    }

    @Override
    public List<AuthorizeType> authorizeTypeData() {
        return new ArrayList<>();
    }

    @Override
    public List<Role> findCollegeRoleByCollegeIdRelation(int collegeId) {
        return new ArrayList<>();
    }

    @Override
    public AjaxUtil<Map<String, Object>> checkAddUsername(String username, String targetUsername, int collegeId) {
        return AjaxUtil.of();
    }

    @Override
    public AjaxUtil<Map<String, Object>> save(AuthorizeAddVo authorizeAddVo) {
        return AjaxUtil.of();
    }

    @Override
    public AjaxUtil<Map<String, Object>> update(AuthorizeEditVo authorizeEditVo) {
        return AjaxUtil.of();
    }

    @Override
    public AjaxUtil<Map<String, Object>> checkEditAccess(String username, String roleApplyId) {
        return AjaxUtil.of();
    }

    @Override
    public AjaxUtil<Map<String, Object>> delete(String username, String roleApplyId) {
        return AjaxUtil.of();
    }

    @Override
    public AjaxUtil<Map<String, Object>> status(String username, String roleApplyId, Byte applyStatus, String refuse) {
        return AjaxUtil.of();
    }
}
