package top.zbeboy.zone.service.educational;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import top.zbeboy.zbase.domain.tables.daos.TimetableSemesterDao;
import top.zbeboy.zbase.domain.tables.pojos.TimetableSemester;
import top.zbeboy.zbase.domain.tables.records.TimetableSemesterRecord;

import javax.annotation.Resource;

import java.util.Optional;

import static top.zbeboy.zbase.domain.Tables.*;

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
    public TimetableSemester findByTimetableSemesterId(String timetableSemesterId) {
        return timetableSemesterDao.findById(timetableSemesterId);
    }

    @Override
    public Result<Record> findAll() {
        return create.select()
        .from(TIMETABLE_SEMESTER)
                .join(COLLEGE)
                .on(TIMETABLE_SEMESTER.COLLEGE_ID.eq(COLLEGE.COLLEGE_ID))
                .join(SCHOOL)
                .on(COLLEGE.SCHOOL_ID.eq(SCHOOL.SCHOOL_ID))
                .orderBy(TIMETABLE_SEMESTER.CODE.desc())
                .fetch();
    }

    @Override
    public Result<Record> findByCollegeId(int collegeId) {
        return create.select()
                .from(TIMETABLE_SEMESTER)
                .join(COLLEGE)
                .on(TIMETABLE_SEMESTER.COLLEGE_ID.eq(COLLEGE.COLLEGE_ID))
                .join(SCHOOL)
                .on(COLLEGE.SCHOOL_ID.eq(SCHOOL.SCHOOL_ID))
                .where(TIMETABLE_SEMESTER.COLLEGE_ID.eq(collegeId))
                .orderBy(TIMETABLE_SEMESTER.CODE.desc())
                .fetch();
    }

    @Override
    public Optional<TimetableSemesterRecord> findByIdAndCollegeId(int id, int collegeId) {
        return create.selectFrom(TIMETABLE_SEMESTER)
                .where(TIMETABLE_SEMESTER.ID.eq(id).and(TIMETABLE_SEMESTER.COLLEGE_ID.eq(collegeId)))
                .fetchOptional();
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
