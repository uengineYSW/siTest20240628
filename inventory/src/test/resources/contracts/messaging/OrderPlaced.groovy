package contracts.messaging
import org.springframework.cloud.contract.spec.Contract

Contract.make {
    // The Identifier which can be used to identify it later.
    label 'OrderPlaced'
    input {
        // Contract will be triggered by the following method.
        triggeredBy('orderPlaced()')
    }
    outputMessage {
        sentTo 'eventTopic'
        // Consumer Expected Payload spec. that a JSON message must have, 
        // If the Producer-side test is OK, then send the following msg to event-out channel.
        body(
            eventType: "OrderPlaced",
                id: 1,
                productId: "1",
                userId: "1",
                qty: 5,
                productName: "TV",
        )
        bodyMatchers {
            jsonPath('$.Id', byRegex(nonEmpty()).asLong())
            jsonPath('$.ProductId', byRegex(nonEmpty()).asString())
            jsonPath('$.UserId', byRegex(nonEmpty()).asString())
            jsonPath('$.Qty', byRegex(nonEmpty()).asInteger())
            jsonPath('$.ProductName', byRegex(nonEmpty()).asString())
        }
        headers {
            messagingContentType(applicationJson())
        }
    }
}