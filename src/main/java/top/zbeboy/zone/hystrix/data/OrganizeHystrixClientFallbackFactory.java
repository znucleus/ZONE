package top.zbeboy.zone.hystrix.data;

import org.springframework.stereotype.Component;
import top.zbeboy.zbase.domain.tables.pojos.Organize;
import top.zbeboy.zone.feign.data.OrganizeService;
import top.zbeboy.zone.web.bean.data.organize.OrganizeBean;
import top.zbeboy.zbase.tools.web.util.AjaxUtil;
import top.zbeboy.zbase.tools.web.util.pagination.DataTablesUtil;
import top.zbeboy.zbase.vo.data.organize.OrganizeAddVo;
import top.zbeboy.zbase.vo.data.organize.OrganizeEditVo;
import top.zbeboy.zbase.vo.data.organize.OrganizeSearchVo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class OrganizeHystrixClientFallbackFactory implements OrganizeService {
    @Override
    public Organize findById(int id) {
        return new Organize();
    }

    @Override
    public int countById(int id) {
        return 0;
    }

    @Override
    public OrganizeBean findByIdRelation(int id) {
        return new OrganizeBean();
    }

    @Override
    public List<OrganizeBean> findNormalByScienceId(int scienceId) {
        return new ArrayList<>();
    }

    @Override
    public List<Organize> findByGradeIdAndOrganizeIsDel(OrganizeSearchVo organizeSearchVo) {
        return new ArrayList<>();
    }

    @Override
    public DataTablesUtil data(DataTablesUtil dataTablesUtil) {
        dataTablesUtil.setData(new ArrayList<>());
        return dataTablesUtil;
    }

    @Override
    public AjaxUtil<Map<String, Object>> checkAddName(String organizeName, int scienceId) {
        return AjaxUtil.of();
    }

    @Override
    public AjaxUtil<Map<String, Object>> checkAddStaff(String staff) {
        return AjaxUtil.of();
    }

    @Override
    public AjaxUtil<Map<String, Object>> save(OrganizeAddVo organizeAddVo) {
        return AjaxUtil.of();
    }

    @Override
    public AjaxUtil<Map<String, Object>> checkEditName(int organizeId, String organizeName, int scienceId) {
        return AjaxUtil.of();
    }

    @Override
    public AjaxUtil<Map<String, Object>> update(OrganizeEditVo organizeEditVo) {
        return AjaxUtil.of();
    }

    @Override
    public AjaxUtil<Map<String, Object>> status(String organizeIds, Byte isDel) {
        return AjaxUtil.of();
    }
}
