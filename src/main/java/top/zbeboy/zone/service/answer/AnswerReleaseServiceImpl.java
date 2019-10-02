package top.zbeboy.zone.service.answer;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service("answerReleaseService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class AnswerReleaseServiceImpl implements AnswerReleaseService {
}
