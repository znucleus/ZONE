package top.zbeboy.zone.service.answer;

import top.zbeboy.zbase.domain.tables.pojos.AnswerRelease;

public interface AnswerReleaseService {

    /**
     * 根据主键查询
     *
     * @param id 主键
     * @return 数据
     */
    AnswerRelease findById(String id);
}
