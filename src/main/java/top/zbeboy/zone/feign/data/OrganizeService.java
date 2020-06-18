package top.zbeboy.zone.feign.data;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import top.zbeboy.zone.domain.tables.pojos.Organize;
import top.zbeboy.zone.hystrix.data.OrganizeHystrixClientFallbackFactory;
import top.zbeboy.zone.web.bean.data.organize.OrganizeBean;
import top.zbeboy.zone.web.util.AjaxUtil;
import top.zbeboy.zone.web.util.pagination.DataTablesUtil;
import top.zbeboy.zone.web.vo.data.organize.OrganizeAddVo;
import top.zbeboy.zone.web.vo.data.organize.OrganizeEditVo;
import top.zbeboy.zone.web.vo.data.organize.OrganizeSearchVo;

import java.util.List;
import java.util.Map;

@FeignClient(value = "base-server", fallback = OrganizeHystrixClientFallbackFactory.class)
public interface OrganizeService {

    /**
     * 获取班级
     *
     * @param id 班级主键
     * @return 班级数据
     */
    @GetMapping("/base/data/organize/{id}")
    Organize findById(@PathVariable("id") int id);

    /**
     * 获取班级数量
     *
     * @param id 班级主键
     * @return 数量
     */
    @GetMapping("/base/data/organize_count/{id}")
    int countById(@PathVariable("id") int id);

    /**
     * 获取班级
     *
     * @param id 班级主键
     * @return 班级数据
     */
    @GetMapping("/base/data/organize_relation/{id}")
    OrganizeBean findByIdRelation(@PathVariable("id") int id);

    /**
     * 根据专业id获取正常班级
     *
     * @param scienceId 专业id
     * @return 数据
     */
    @GetMapping("/base/data/organizes_normal/science_id/{scienceId}")
    List<OrganizeBean> findNormalByScienceId(@PathVariable("scienceId") int scienceId);

    /**
     * 获取年级下全部有效班级
     *
     * @param organizeSearchVo 查询参数
     * @return 班级数据
     */
    @PostMapping("/base/data/organizes/search")
    List<Organize> findByGradeIdAndOrganizeIsDel(@RequestBody OrganizeSearchVo organizeSearchVo);

    /**
     * 数据
     *
     * @param dataTablesUtil 请求
     * @return 数据
     */
    @PostMapping("/base/data/organizes/paging")
    DataTablesUtil data(@RequestBody DataTablesUtil dataTablesUtil);

    /**
     * 保存时检验班级名是否重复
     *
     * @param organizeName 班级名
     * @param scienceId    专业id
     * @return true 合格 false 不合格
     */
    @PostMapping("/base/data/organize/check/add/name")
    AjaxUtil<Map<String, Object>> checkAddName(@RequestParam("organizeName") String organizeName, @RequestParam("scienceId") int scienceId);

    /**
     * 检验教职工账号
     *
     * @param staff 账号/工号
     * @return true or false
     */
    @PostMapping("/base/data/organize/check/add/staff")
    AjaxUtil<Map<String, Object>> checkAddStaff(@RequestParam("staff") String staff);

    /**
     * 保存班级信息
     *
     * @param organizeAddVo 班级
     * @return true 保存成功 false 保存失败
     */
    @PostMapping("/base/data/organize/save")
    AjaxUtil<Map<String, Object>> save(@RequestBody OrganizeAddVo organizeAddVo);

    /**
     * 更新时检验班级名是否重复
     *
     * @param organizeId   班级id
     * @param organizeName 班级名
     * @param scienceId    专业id
     * @return true 合格 false 不合格
     */
    @PostMapping("/base/data/organize/check/edit/name")
    AjaxUtil<Map<String, Object>> checkEditName(@RequestParam("organizeId") int organizeId, @RequestParam("organizeName") String organizeName, @RequestParam("scienceId") int scienceId);

    /**
     * 更新班级信息
     *
     * @param organizeEditVo 班级
     * @return true 保存成功 false 保存失败
     */
    @PostMapping("/base/data/organize/update")
    AjaxUtil<Map<String, Object>> update(@RequestBody OrganizeEditVo organizeEditVo);

    /**
     * 批量更改班级状态
     *
     * @param organizeIds 班级ids
     * @param isDel       is_del
     * @return true注销成功
     */
    @PostMapping("/base/data/organizes/status")
    AjaxUtil<Map<String, Object>> status(@RequestParam(value = "organizeIds", required = false) String organizeIds, @RequestParam("isDel") Byte isDel);
}
