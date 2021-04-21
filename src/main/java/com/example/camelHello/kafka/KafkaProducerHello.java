package com.example.camelHello.kafka;

import com.example.camelHello.ChoiceCamel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.model.ModelCamelContext;
import org.apache.camel.model.RouteDefinition;

/**
 * @author zhengweibing3
 * @version V1.0.0
 * @description:
 * @date 2021/4/20/0020 14:37
 * @copyright Copyright © 2019  智能城市icity.jd.com ALL Right Reserved
 */
public class KafkaProducerHello {

    public static void main(String[] args) throws Exception{
        ModelCamelContext camelContext = new DefaultCamelContext();
        camelContext.addRoutes(new RouteBuilder() {
            @Override
            public void configure() {
                RouteDefinition a = from("timer:simple?period=1000");
                a.setBody(constant("Message produce from Camel to kafka"));
                a.log("这都能跑？");
                a.setId("1");
//                a.setHeader("hello", constant("Camel"));
//                a.to("kafka:cameltest?brokers=localhost:9092").routeId("1");
//                from("timer:simple?period=1000")
//                    .setBody(constant("Message produce from Camel to kafka"))          // Message to send
//                    .setHeader("hello", constant("Camel")) // Key of the message
//                    .to("kafka:cameltest?brokers=localhost:9092");
            }
        });
        camelContext.start();
        Thread.sleep(1000);
        camelContext.stop();
        camelContext.removeRoute("1");
        // 通用没有具体业务意义的代码，只是为了保证主线程不退出
        synchronized (ChoiceCamel.class) {
            ChoiceCamel.class.wait();
        }
    }
}
