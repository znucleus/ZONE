package top.zbeboy.zone.test;

import org.apache.commons.pool2.BaseKeyedPooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

public class HttpClientObjectFactory extends BaseKeyedPooledObjectFactory<String, HttpClientObject> {
    @Override
    public HttpClientObject create(String s) throws Exception {
        return new HttpClientObject(s);
    }

    @Override
    public PooledObject<HttpClientObject> wrap(HttpClientObject httpClientObject) {
        return new DefaultPooledObject<>(httpClientObject);
    }
}
