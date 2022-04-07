package com.chuan.config;

import com.chuan.client.Serializer;
import com.chuan.enums.AlgorithmEnum;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 读取配置文件
 *
 * @author chuan
 */
public abstract class Config {
    static Properties properties;

    static {
        try (InputStream in = Config.class.getResourceAsStream("/application.properties")) {
            properties = new Properties();
            properties.load(in);
        }
        catch (IOException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public static int getServerPort() {
        String value = properties.getProperty("server.port");
        if (value == null) {
            return 8080;
        }
        else {
            return Integer.parseInt(value);
        }
    }

    public static AlgorithmEnum getSerializerAlgorithm() {
        String value = properties.getProperty("serializer.algorithm");
        if (value == null) {
            return AlgorithmEnum.Java;
        }
        else {
            return AlgorithmEnum.valueOf(value);
        }
    }
}