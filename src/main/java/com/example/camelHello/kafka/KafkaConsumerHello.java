package com.example.camelHello.kafka;

import com.example.camelHello.ChoiceCamel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.properties.PropertiesComponent;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.model.ModelCamelContext;

/**
 * @author zhengweibing3
 * @version V1.0.0
 * @description:
 * @date 2021/4/20/0020 14:30
 * @copyright Copyright © 2019  智能城市icity.jd.com ALL Right Reserved
 */
public class KafkaConsumerHello {

    public static void main(String[] args) throws Exception{
        ModelCamelContext camelContext = new DefaultCamelContext();

        camelContext.addRoutes(new RouteBuilder() {
            @Override
            public void configure() {
                from("kafka:cameltest?brokers=localhost:9092")
                        .log("Message received from Kafka : ${body}")
                        .log("    on the topic ${headers[kafka.TOPIC]}")
                        .log("    on the partition ${headers[kafka.PARTITION]}")
                        .log("    with the offset ${headers[kafka.OFFSET]}")
                        .log("    with the key ${headers[kafka.KEY]}");
            }
        });
        camelContext.start();
        // 通用没有具体业务意义的代码，只是为了保证主线程不退出
        synchronized (ChoiceCamel.class) {
            ChoiceCamel.class.wait();
        }
    }
}
