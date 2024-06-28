package com.example.template;

import edittemplate.DecreaseStockApplication;
import edittemplate.domain.*;
import edittemplate.infra.DecreaseStockController;
import java.util.concurrent.TimeUnit;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.verifier.messaging.MessageVerifier;
import org.springframework.cloud.contract.verifier.messaging.boot.AutoConfigureMessageVerifier;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.MimeTypeUtils;

@RunWith(SpringRunner.class)
@SpringBootTest(
    classes = OrderApplication.class,
    webEnvironment = SpringBootTest.WebEnvironment.NONE
)
@AutoConfigureMessageVerifier
public abstract class MessagingBase {

    @Autowired
    OrderController orderController;

    @Autowired
    // Message interface to verify Contracts between services.
    MessageVerifier messaging;

    @Before
    public void setup() {
        // any remaining messages on the "eventTopic" channel are cleared
        // makes that each test starts with a clean slate
        this.messaging.receive("eventTopic", 100, TimeUnit.MILLISECONDS);
    }

    public void orderPlaced() {
        Order order = new Order();

        order.setId(1L);
        order.setProductId("1");
        order.setUserId("1");
        order.setQty(5);
        order.setProductName("TV");

        OrderPlaced orderPlaced = new OrderPlaced(order);
        // orderPlaced.setEventType("OrderPlaced");

        serializedJson = orderPlaced.toJson();

        this.messaging.send(
                MessageBuilder
                    .withPayload(serializedJson)
                    .setHeader(
                        MessageHeaders.CONTENT_TYPE,
                        MimeTypeUtils.APPLICATION_JSON
                    )
                    .build(),
                "eventTopic"
            );
    }
}
