package top.zbeboy.zone.web.achievement.software;

import org.apache.commons.pool2.BaseKeyedPooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

public class SoftwareHttpClientFactory extends BaseKeyedPooledObjectFactory<String, SoftwareHttpClient> {
    @Override
    public SoftwareHttpClient create(String s){
        return new SoftwareHttpClient();
    }

    @Override
    public PooledObject<SoftwareHttpClient> wrap(SoftwareHttpClient softwareHttpClient) {
        return new DefaultPooledObject<>(softwareHttpClient);
    }
}
