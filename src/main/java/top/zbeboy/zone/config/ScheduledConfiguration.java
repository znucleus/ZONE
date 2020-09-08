package top.zbeboy.zone.config;

import org.jooq.Record;
import org.jooq.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import top.zbeboy.zbase.config.Workbook;
import top.zbeboy.zbase.domain.tables.pojos.TrainingAttend;
import top.zbeboy.zbase.domain.tables.pojos.TrainingAttendUsers;
import top.zbeboy.zbase.domain.tables.pojos.TrainingConfigure;
import top.zbeboy.zbase.domain.tables.pojos.TrainingUsers;
import top.zbeboy.zbase.tools.service.util.DateTimeUtil;
import top.zbeboy.zbase.tools.service.util.UUIDUtil;
import top.zbeboy.zbase.tools.web.util.ByteUtil;
import top.zbeboy.zone.service.internship.InternshipApplyService;
import top.zbeboy.zone.service.training.TrainingAttendService;
import top.zbeboy.zone.service.training.TrainingAttendUsersService;
import top.zbeboy.zone.service.training.TrainingConfigureService;
import top.zbeboy.zone.service.training.TrainingUsersService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 定时任务配置
 *
 * @author zbeboy
 * @version 1.0
 * 例子:
 * 0 0 10,14,16 * * ? 每天上午10点，下午2点，4点
 * 0 0/30 9-17 * * ?   朝九晚五工作时间内每半小时
 * "0 0 12 * * ?" 每天中午12点触发
 * "0 15 10 ? * *" 每天上午10:15触发
 * "0 15 10 * * ?" 每天上午10:15触发
 * "0 15 10 * * ? *" 每天上午10:15触发
 * "0 15 10 * * ? 2005" 2005年的每天上午10:15触发
 * "0 * 14 * * ?" 在每天下午2点到下午2:59期间的每1分钟触发
 * "0 0/5 14 * * ?" 在每天下午2点到下午2:55期间的每5分钟触发
 * "0 0/5 14,18 * * ?" 在每天下午2点到2:55期间和下午6点到6:55期间的每5分钟触发
 * "0 0-5 14 * * ?" 在每天下午2点到下午2:05期间的每1分钟触发
 * "0 10,44 14 ? 3 WED" 每年三月的星期三的下午2:10和2:44触发
 * "0 15 10 15 * ?" 每月15日上午10:15触发
 * "0 15 10 ? * 6L" 每月的最后一个星期五上午10:15触发
 * "0 15 10 ? * 6L 2002-2005" 2002年至2005年的每月的最后一个星期五上午10:15触发
 * "0 15 10 ? * 6#3" 每月的第三个星期五上午10:15触发
 * <p>
 * 注：不支持字符L等等字符
 */
@Configuration
@EnableScheduling
public class ScheduledConfiguration {

    private final Logger log = LoggerFactory.getLogger(ScheduledConfiguration.class);

    @Resource
    private Environment env;

    @Resource
    private InternshipApplyService internshipApplyService;

    @Resource
    private TrainingConfigureService trainingConfigureService;

    @Resource
    private TrainingAttendService trainingAttendService;

    @Resource
    private TrainingUsersService trainingUsersService;

    @Resource
    private TrainingAttendUsersService trainingAttendUsersService;

    /**
     * 更改实习状态为申请中
     */
    @Scheduled(cron = "0 15 02 * * ?") // 每天 晚间2点15分
    public void internshipApply() {
        if (env.acceptsProfiles(Profiles.of(Workbook.SPRING_PROFILE_DEVELOPMENT, Workbook.SPRING_PROFILE_PRODUCTION))) {
            // 更改实习提交状态
            internshipApplyService.updateState(0, 1);
            List<Integer> states = new ArrayList<>();
            states.add(5);
            states.add(7);
            internshipApplyService.updateChangeState(states, 1);
        }
    }

    /**
     * 自动生成实训考勤数据
     */
    @Scheduled(cron = "0 30 00 * * ?") // 每天 晚间12点30分
    public void generateTrainingAttend() {
        if (env.acceptsProfiles(Profiles.of(Workbook.SPRING_PROFILE_DEVELOPMENT, Workbook.SPRING_PROFILE_PRODUCTION))) {
            Result<Record> records = trainingConfigureService.findIsAuto(ByteUtil.toByte(DateTimeUtil.getNowDayOfWeek()));
            if (records.isNotEmpty()) {
                List<TrainingConfigure> trainingConfigures = records.into(TrainingConfigure.class);
                for (TrainingConfigure trainingConfigure : trainingConfigures) {
                    TrainingAttend trainingAttend = new TrainingAttend();
                    String trainingAttendId = UUIDUtil.getUUID();
                    trainingAttend.setTrainingAttendId(trainingAttendId);
                    trainingAttend.setTrainingReleaseId(trainingConfigure.getTrainingReleaseId());
                    trainingAttend.setAttendDate(DateTimeUtil.getNowSqlDate());
                    trainingAttend.setAttendStartTime(trainingConfigure.getStartTime());
                    trainingAttend.setAttendEndTime(trainingConfigure.getEndTime());
                    trainingAttend.setAttendRoom(trainingConfigure.getSchoolroomId());
                    trainingAttend.setPublishDate(DateTimeUtil.getNowSqlTimestamp());
                    trainingAttendService.save(trainingAttend);

                    List<TrainingUsers> trainingUsers = trainingUsersService.findByTrainingReleaseId(trainingConfigure.getTrainingReleaseId());
                    if (Objects.nonNull(trainingUsers)) {
                        List<TrainingAttendUsers> trainingAttendUsers = new ArrayList<>();
                        for (TrainingUsers users : trainingUsers) {
                            TrainingAttendUsers trainingAttendUser = new TrainingAttendUsers();
                            trainingAttendUser.setAttendUsersId(UUIDUtil.getUUID());
                            trainingAttendUser.setTrainingAttendId(trainingAttendId);
                            trainingAttendUser.setTrainingUsersId(users.getTrainingUsersId());
                            trainingAttendUser.setOperateUser(Workbook.username.actuator.name());
                            trainingAttendUser.setOperateDate(DateTimeUtil.getNowSqlTimestamp());
                            trainingAttendUser.setOperate(ByteUtil.toByte(0));
                            trainingAttendUser.setRemark(users.getRemark());

                            trainingAttendUsers.add(trainingAttendUser);
                        }

                        trainingAttendUsersService.batchSave(trainingAttendUsers);
                    }
                }
            }
            log.info(">>>>>>>>>>>>> scheduled ... generate training attend ");
        }
    }

}
