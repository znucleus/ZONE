package top.zbeboy.zone.feign.data;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import top.zbeboy.zone.domain.tables.pojos.College;
import top.zbeboy.zone.domain.tables.pojos.CollegeApplication;
import top.zbeboy.zone.domain.tables.pojos.School;
import top.zbeboy.zone.hystrix.data.CollegeHystrixClientFallbackFactory;
import top.zbeboy.zone.web.bean.data.college.CollegeBean;
import top.zbeboy.zone.web.plugin.treeview.TreeViewData;
import top.zbeboy.zone.web.util.AjaxUtil;
import top.zbeboy.zone.web.util.pagination.DataTablesUtil;
import top.zbeboy.zone.web.vo.data.college.CollegeAddVo;
import top.zbeboy.zone.web.vo.data.college.CollegeEditVo;
import top.zbeboy.zone.web.vo.data.college.CollegeSearchVo;

import java.util.List;
import java.util.Map;

@FeignClient(value = "base-server", fallback = CollegeHystrixClientFallbackFactory.class)
public interface CollegeService {

    /**
     * 获取院
     *
     * @param id 院主键
     * @return 院数据
     */
    @GetMapping("/base/data/college/{id}")
    College findById(@PathVariable("id") int id);

    /**
     * 获取院
     *
     * @param id 院主键
     * @return 院数据
     */
    @GetMapping("/base/data/college/relation/{id}")
    CollegeBean findByIdRelation(@PathVariable("id") int id);

    /**
     * 获取学校下全部有效院
     *
     * @param collegeSearchVo 查询参数
     * @return 院数据
     */
    @PostMapping("/base/anyone/data/college/all")
    List<College> anyoneData(@RequestBody CollegeSearchVo collegeSearchVo);

    /**
     * 数据
     *
     * @param dataTablesUtil 请求
     * @return 数据
     */
    @PostMapping("/base/data/college/data")
    DataTablesUtil data(@RequestBody DataTablesUtil dataTablesUtil);

    /**
     * 保存时检验院名是否重复
     *
     * @param collegeName 院名
     * @param schoolId    学校id
     * @return true 合格 false 不合格
     */
    @PostMapping("/base/data/college/check/add/name")
    AjaxUtil<Map<String, Object>> checkAddName(@RequestParam("collegeName") String collegeName, @RequestParam("schoolId") int schoolId);

    /**
     * 检验院代码是否重复
     *
     * @param collegeCode 院代码
     * @return true 合格 false 不合格
     */
    @PostMapping("/base/data/college/check/add/code")
    AjaxUtil<Map<String, Object>> checkAddCode(@RequestParam("collegeCode") String collegeCode);

    /**
     * 保存院信息
     *
     * @param collegeAddVo 院
     * @return true 保存成功 false 保存失败
     */
    @PostMapping("/base/data/college/save")
    AjaxUtil<Map<String, Object>> save(@RequestBody CollegeAddVo collegeAddVo);

    /**
     * 检验编辑时院名重复
     *
     * @param collegeId   院id
     * @param collegeName 院名
     * @param schoolId    学校id
     * @return true 合格 false 不合格
     */
    @PostMapping("/base/data/college/check/edit/name")
    AjaxUtil<Map<String, Object>> checkEditName(@RequestParam("collegeId") int collegeId, @RequestParam("collegeName") String collegeName, @RequestParam("schoolId") int schoolId);

    /**
     * 检验编辑时院代码重复
     *
     * @param collegeId   院id
     * @param collegeCode 院代码
     * @return true 合格 false 不合格
     */
    @PostMapping("/base/data/college/check/edit/code")
    AjaxUtil<Map<String, Object>> checkEditCode(@RequestParam("collegeId") int collegeId, @RequestParam("collegeCode") String collegeCode);

    /**
     * 保存院更改
     *
     * @param collegeEditVo 院
     * @return true 更改成功 false 更改失败
     */
    @PostMapping("/base/data/college/update")
    AjaxUtil<Map<String, Object>> update(@RequestBody CollegeEditVo collegeEditVo);

    /**
     * 批量更改院状态
     *
     * @param collegeIds 院ids
     * @param isDel      is_del
     * @return true注销成功
     */
    @PostMapping("/base/data/college/status")
    AjaxUtil<Map<String, Object>> status(@RequestParam(value = "collegeIds", required = false) String collegeIds, @RequestParam("isDel") Byte isDel);

    /**
     * 数据json
     *
     * @return json
     */
    @GetMapping("/base/data/college/application/json")
    List<TreeViewData> applicationJson();

    /**
     * 院与应用数据
     *
     * @param collegeId 院id
     * @return 数据
     */
    @PostMapping("/base/data/college/application/data")
    List<CollegeApplication> collegeApplicationData(@RequestParam("collegeId") int collegeId);

    /**
     * 更新应用挂载
     *
     * @param collegeId      院id
     * @param applicationIds 应用ids
     * @return true 更新成功
     */
    @PostMapping("/base/data/college/mount")
    AjaxUtil<Map<String, Object>> mount(@RequestParam("collegeId") int collegeId, @RequestParam(value = "applicationIds", required = false) String applicationIds);
}
