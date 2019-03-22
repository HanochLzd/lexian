package com.lexian.utils;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.pool.KryoFactory;
import com.esotericsoftware.kryo.pool.KryoPool;
import org.objenesis.strategy.StdInstantiatorStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * @author zhaomingyuan
 * @date 18-6-8
 * @time 上午11:00
 */
public class KryoUtils {
    private static final Logger LOG = LoggerFactory.getLogger(KryoUtils.class);


    private static KryoFactory factory = () -> {
        Kryo kryo = new Kryo();
        // configure kryo instance, customize settings
        //支持对象循环引用（否则会栈溢出）
        kryo.setReferences(true); //默认值就是 true，添加此行的目的是为了提醒维护者，不要改变这个配置

        //不强制要求注册类（注册行为无法保证多个 JVM 内同一个类的注册编号相同；而且业务系统中大量的 Class 也难以一一注册）
        kryo.setRegistrationRequired(false); //默认值就是 false，添加此行的目的是为了提醒维护者，不要改变这个配置

        //Fix the NPE bug when deserializing Collections.
        ((Kryo.DefaultInstantiatorStrategy) kryo.getInstantiatorStrategy())
                .setFallbackInstantiatorStrategy(new StdInstantiatorStrategy());
        return kryo;
    };

    private static KryoPool pool = new KryoPool.Builder(factory).softReferences().build();

    /**
     * 序列化
     *
     * @param obj
     * @return
     */
    public static byte[] serialize(final Object obj) {
        return pool.run(kryo -> {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            Output output = new Output(byteArrayOutputStream);
            try {
                kryo.writeClassAndObject(output, obj);
                output.flush();
                return byteArrayOutputStream.toByteArray();
            } catch (Exception e) {
                LOG.error("kyro serialize error", e);
                return null;
            }

        });
    }

    /**
     * 反序列化
     *
     * @param byteArray
     * @return
     */
    public static Object unSerialize(final byte[] byteArray) {
        return pool.run(kryo -> {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);
            Input input = new Input(byteArrayInputStream);
            try {
                Object obj = kryo.readClassAndObject(input);
                if (obj != null) {
                    return obj;
                }
                return null;
            } catch (Exception e) {
                LOG.error("kyro deserialize error", e);
                return null;
            }
        });
    }
}
