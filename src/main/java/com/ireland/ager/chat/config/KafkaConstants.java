package com.ireland.ager.chat.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;

@PropertySource("classpath:kafka.properties")
public class KafkaConstants {

    public static final String KAFKA_TOPIC = "ager";
    public static final String GROUP_ID = "project";
    public static final String KAFKA_BROKER = "localhost:9092";

}
