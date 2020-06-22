package top.zbeboy.zone.service.answer;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import top.zbeboy.zbase.domain.tables.daos.AnswerReleaseDao;
import top.zbeboy.zbase.domain.tables.pojos.AnswerRelease;

import javax.annotation.Resource;

@Service("answerReleaseService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class AnswerReleaseServiceImpl implements AnswerReleaseService {

    @Resource
    private AnswerReleaseDao answerReleaseDao;

    @Override
    public AnswerRelease findById(String id) {
        return answerReleaseDao.findById(id);
    }
}
