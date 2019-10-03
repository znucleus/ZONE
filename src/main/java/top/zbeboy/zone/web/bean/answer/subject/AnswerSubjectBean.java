package top.zbeboy.zone.web.bean.answer.subject;

import top.zbeboy.zone.domain.tables.pojos.AnswerOption;
import top.zbeboy.zone.domain.tables.pojos.AnswerSubject;

import java.util.List;

public class AnswerSubjectBean extends AnswerSubject {
    private List<AnswerOption> options;

    public List<AnswerOption> getOptions() {
        return options;
    }

    public void setOptions(List<AnswerOption> options) {
        this.options = options;
    }
}
