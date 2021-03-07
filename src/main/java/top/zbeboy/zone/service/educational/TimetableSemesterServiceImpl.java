package top.zbeboy.zone.service.educational;

import org.jooq.DSLContext;
import org.jooq.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import top.zbeboy.zbase.domain.tables.daos.TimetableSemesterDao;
import top.zbeboy.zbase.domain.tables.pojos.TimetableSemester;
import top.zbeboy.zbase.domain.tables.records.TimetableSemesterRecord;

import javax.annotation.Resource;

import static top.zbeboy.zbase.domain.Tables.TIMETABLE_SEMESTER;

@Service("timetableSemesterService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class TimetableSemesterServiceImpl implements TimetableSemesterService {

    private final DSLContext create;

    @Autowired
    TimetableSemesterServiceImpl(DSLContext dslContext) {
        create = dslContext;
    }

    @Resource
    private TimetableSemesterDao timetableSemesterDao;

    @Override
    public Result<TimetableSemesterRecord> findAll() {
        return create.selectFrom(TIMETABLE_SEMESTER)
                .orderBy(TIMETABLE_SEMESTER.CODE.desc())
                .fetch();
    }

    @Override
    public TimetableSemester findById(int id) {
        return timetableSemesterDao.findById(id);
    }

    @Override
    public void update(TimetableSemester timetableSemester) {
        timetableSemesterDao.update(timetableSemester);
    }

    @Override
    public void save(TimetableSemester timetableSemester) {
        timetableSemesterDao.insert(timetableSemester);
    }
}
