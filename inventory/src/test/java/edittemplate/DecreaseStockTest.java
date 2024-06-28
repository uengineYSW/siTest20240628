package edittemplate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edittemplate.config.kafka.KafkaProcessor;
import edittemplate.domain.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.cloud.stream.test.binder.MessageCollector;
import org.springframework.context.ApplicationContext;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.MimeTypeUtils;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DecreaseStockTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(
        DecreaseStockTest.class
    );

    @Autowired
    private KafkaProcessor processor;

    @Autowired
    private MessageCollector messageCollector;

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    @SuppressWarnings("unchecked")
    public void test0() {
        //given:

        entity.setId("1");
        entity.setStock("10");

        repository.save(entity);

        //when:

        OrderPlaced event = new OrderPlaced();

        event.setId("1");
        event.setProductId("1");
        event.setUserId("1");
        event.setQty("5");
        event.setProductName("TV");

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String msg = objectMapper.writeValueAsString(event);

            processor
                .inboundTopic()
                .send(
                    MessageBuilder
                        .withPayload(msg)
                        .setHeader(
                            MessageHeaders.CONTENT_TYPE,
                            MimeTypeUtils.APPLICATION_JSON
                        )
                        .setHeader("type", event.getEventType())
                        .build()
                );

            //then:

            Message<String> received = (Message<String>) messageCollector
                .forChannel(processor.outboundTopic())
                .poll();

            assertNotNull("Resulted event must be published", received);

            StockDecreased outputEvent = objectMapper.readValue(
                received.getPayload(),
                StockDecreased.class
            );

            LOGGER.info("Response received: {}", received.getPayload());

            assertEquals(outputEvent.getId(), "1");
            assertEquals(outputEvent.getStock(), "5");
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            assertTrue("exception", false);
        }
    }
}
