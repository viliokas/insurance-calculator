package com.insurance.insurance;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.insurance.insurance.models.Policy;
import com.insurance.insurance.models.PolicyObject;
import com.insurance.insurance.models.PolicySubObject;
import com.insurance.insurance.models.response.PolicyPrice;
import com.insurance.insurance.utils.FileHelper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class InsuranceApplicationTests {
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void integrationOkTest() throws Exception {
		PolicyPrice expectedResult = PolicyPrice.builder()
				.price(2.28)
				.build();

		String testRequestContent = FileHelper.readRequestFileToString("calculate-insurance.json");
		MvcResult result = mockMvc.perform(post("/api/calculate-insurance", 42L)
				.contentType(MediaType.APPLICATION_JSON)
				.content(testRequestContent))
				.andExpect(status().isOk()).andReturn();

		PolicyPrice price = objectMapper.readValue(result.getResponse().getContentAsString(),PolicyPrice.class);
		assertThat(price).isEqualTo(expectedResult);
	}

	@Test
	void integrationBadRequestTest() throws Exception {
		PolicySubObject policySubObject1 = PolicySubObject.builder()
				.riskType("FIREE")
				.sumInsurance(100.00)
				.subObjectName("policySubObject1")
				.build();
		PolicySubObject policySubObject2 = PolicySubObject.builder()
				.riskType("THEFT")
				.sumInsurance(8.00)
				.subObjectName("policySubObject2")
				.build();
		PolicyObject policyObject1 = PolicyObject.builder()
				.objectName("policyObject1")
				.policySubObjectList(List.of(policySubObject1, policySubObject2))
				.build();
		Policy policy = Policy.builder()
				.policyNumber("LV00-00-000000-1")
				.policyStatus("REGISTERED")
				.policyObjectList(List.of(policyObject1))
				.build();

		MvcResult result = mockMvc.perform(post("/api/calculate-insurance", 42L)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(policy)))
				.andExpect(status().isBadRequest()).andReturn();

		assertThat(result.getResponse().getContentAsString().contains("Risk type FIREE not found")).isEqualTo(true);
	}

}
