package com.example.template;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import edittemplate.InventoryApplication;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = edittemplate.InventoryApplication.class)
@AutoConfigureMockMvc
@AutoConfigureStubRunner(
    stubsMode = StubRunnerProperties.StubsMode.LOCAL,
    ids = "edittemplate::+:stubs:8090"
)
@ActiveProfiles("test")
public class ContractTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    public void get_stub_test() throws Exception {
        MvcResult result = mockMvc
            .perform(
                MockMvcRequestBuilders
                    .get("/inventory/validate/1")
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andReturn();

        String responseString = result.getResponse().getContentAsString();
        DocumentContext parsedJson = JsonPath.parse(responseString);
        // and:
        // examples
    }
}
