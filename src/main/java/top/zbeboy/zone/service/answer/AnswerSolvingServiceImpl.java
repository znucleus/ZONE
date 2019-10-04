package top.zbeboy.zone.service.answer;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import top.zbeboy.zone.domain.tables.daos.AnswerSolvingDao;
import top.zbeboy.zone.domain.tables.pojos.AnswerSolving;

import javax.annotation.Resource;
import java.util.List;

@Service("answerSolvingService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class AnswerSolvingServiceImpl implements AnswerSolvingService{

    @Resource
    private AnswerSolvingDao answerSolvingDao;

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void save(List<AnswerSolving> answerSolvings) {
        answerSolvingDao.insert(answerSolvings);
    }
}
