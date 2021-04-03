package top.zbeboy.zone.web.achievement.student;

import org.apache.commons.pool2.BaseKeyedPooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

public class StudentAchievementHttpClientFactory extends BaseKeyedPooledObjectFactory<String, StudentAchievementHttpClient> {
    @Override
    public StudentAchievementHttpClient create(String s) throws Exception {
        return new StudentAchievementHttpClient();
    }

    @Override
    public PooledObject<StudentAchievementHttpClient> wrap(StudentAchievementHttpClient studentAchievementHttpClient) {
        return new DefaultPooledObject<>(studentAchievementHttpClient);
    }
}
