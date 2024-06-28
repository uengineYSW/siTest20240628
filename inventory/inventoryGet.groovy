package contracts.rest

org.springframework.cloud.contract.spec.Contract.make {
    request {
        method ''
        url ('/')
        headers {
            contentType(applicationJsonUtf8())
        }
        body(
        )
    }
    response {
        status 200
        body(
        )
        bodyMatchers {
        }
        headers {
            contentType(applicationJsonUtf8())
        }
    }
}

