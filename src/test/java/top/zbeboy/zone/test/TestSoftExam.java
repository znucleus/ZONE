package top.zbeboy.zone.test;

import org.apache.commons.pool2.KeyedObjectPool;
import org.apache.commons.pool2.impl.GenericKeyedObjectPool;
import org.junit.jupiter.api.Test;

public class TestSoftExam {

    @Test
    public void testExam() {
        KeyedObjectPool<String, HttpClientObject> objectPool = new GenericKeyedObjectPool<>(new HttpClientObjectFactory());
        try {
            //添加对象到池，重复的不会重复入池
            objectPool.addObject("1");

            // 获得对应key的对象
            HttpClientObject httpClientObject = objectPool.borrowObject("1");
            System.out.println(httpClientObject);
            httpClientObject.getHttpclient().close();
            // 释放对象
            objectPool.returnObject("1", httpClientObject);

            //清除所有的对象
            objectPool.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
