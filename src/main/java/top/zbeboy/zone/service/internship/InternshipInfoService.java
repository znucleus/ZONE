package top.zbeboy.zone.service.internship;

import top.zbeboy.zone.web.vo.internship.apply.InternshipApplyAddVo;

public interface InternshipInfoService {

    /**
     * jooq事务性保存
     *
     * @param internshipApplyAddVo 数据
     */
    void saveWithTransaction(InternshipApplyAddVo internshipApplyAddVo);
}
