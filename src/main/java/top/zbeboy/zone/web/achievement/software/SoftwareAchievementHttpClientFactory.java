package top.zbeboy.zone.web.achievement.software;

import org.apache.commons.pool2.BaseKeyedPooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

public class SoftwareAchievementHttpClientFactory extends BaseKeyedPooledObjectFactory<String, SoftwareAchievementHttpClient> {
    @Override
    public SoftwareAchievementHttpClient create(String s){
        return new SoftwareAchievementHttpClient();
    }

    @Override
    public PooledObject<SoftwareAchievementHttpClient> wrap(SoftwareAchievementHttpClient softwareAchievementHttpClient) {
        return new DefaultPooledObject<>(softwareAchievementHttpClient);
    }
}
