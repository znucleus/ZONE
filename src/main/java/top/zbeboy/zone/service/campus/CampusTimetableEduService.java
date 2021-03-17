package top.zbeboy.zone.service.campus;

import top.zbeboy.zbase.domain.tables.pojos.CampusCourseData;

import java.io.IOException;
import java.util.List;

public interface CampusTimetableEduService {

    /**
     * 生成日历
     *
     * @param campusCourseDataList 数据
     * @param path                 ics路径
     */
    void generateIcs(List<CampusCourseData> campusCourseDataList, String path) throws IOException;
}
