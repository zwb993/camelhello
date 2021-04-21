package com.example.camelHello;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.main.Main;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Objects;

/**
 * @author zhengweibing3
 * @version V1.0.0
 * @description:
 * @date 2021/4/19/0019 15:01
 * @copyright Copyright © 2019  智能城市icity.jd.com ALL Right Reserved
 */
public class MyApplication {
    private static final Logger LOGGER = LoggerFactory.getLogger(MyApplication.class);

    private MyApplication() {
    }

    public static void main(String[] args) throws Exception {
        Main main = new Main();
        main.configure().addRoutesBuilder(new MyRouteBuilder());
        main.run(args);
    }

    private static class MyRouteBuilder extends RouteBuilder {
        @Override
        public void configure() throws Exception {
            from("timer:simple?period=503")
                    .id("simple-route")
                    .transform()
                    .exchange(this::dateToTime)
                    .process()
                    .message(this::log)
                    .process()
                    .body(this::log)
                    .choice()
                    .when()
                    .body(Integer.class, b -> (b & 1) == 0)
                    .log("Received even number")
                    .when()
                    .body(Integer.class, (b, h) -> h.containsKey("skip") ? false : (b & 1) == 0)
                    .log("Received odd number")
                    .when()
                    .body(Objects::isNull)
                    .log("Received null body")
                    .when()
                    .body(Integer.class, b -> (b & 1) != 0)
                    .log("Received odd number")
                    .endChoice();
        }

        private Long dateToTime(Exchange e) {
            return e.getProperty(Exchange.TIMER_FIRED_TIME, Date.class).getTime();
        }

        private void log(Object b) {
            LOGGER.info("body is: {}", b);
        }

        private void log(Message m) {
            LOGGER.info("message is: {}", m);
        }
    }
}
