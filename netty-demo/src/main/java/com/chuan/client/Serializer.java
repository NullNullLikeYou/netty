package com.chuan.client;

/**
 * 用于扩展序列化、反序列化算法
 *
 * @author chuan
 */
public interface Serializer {

    /**
     * 反序列化方法
     *
     * @param clazz clazz
     * @param bytes bytes
     * @param <T>   T
     * @return 对象
     */
    <T> T deserialize(Class<T> clazz, byte[] bytes);

    /**
     * 序列化方法
     *
     * @param object object
     * @param <T>    对象
     * @return 对象
     */
    <T> byte[] serialize(T object);
}