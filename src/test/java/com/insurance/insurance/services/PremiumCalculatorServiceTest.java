package com.insurance.insurance.services;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.insurance.insurance.exceptions.RiskTypeNotFoundException;
import com.insurance.insurance.models.Policy;
import com.insurance.insurance.models.PolicyObject;
import com.insurance.insurance.models.PolicySubObject;
import com.insurance.insurance.models.response.PolicyPrice;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.text.ParseException;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
public class PremiumCalculatorServiceTest {
    @InjectMocks
    PremiumCalculatorService premiumCalculatorService;

    @Test
    void calculateWhenRiskAreLowerThanBreakpointsTest() {
        PolicySubObject policySubObject1 = PolicySubObject.builder()
                .riskType("FIRE")
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
        Policy policy1 = Policy.builder()
                .policyNumber("LV00-00-000000-1")
                .policyStatus("REGISTERED")
                .policyObjectList(List.of(policyObject1))
                .build();
        PolicyPrice expectedResult = PolicyPrice.builder()
                .price(2.28)
                .currency("EUR")
                .build();
        assertThat(premiumCalculatorService.calculate(policy1)).isEqualTo(expectedResult);
    }

    @Test
    void calculateWhenRiskAreHigherThanBreakpointsTest(){
        PolicySubObject policySubObject1 = PolicySubObject.builder()
                .riskType("FIRE")
                .sumInsurance(500.00)
                .subObjectName("policySubObject1")
                .build();
        PolicySubObject policySubObject2 = PolicySubObject.builder()
                .riskType("THEFT")
                .sumInsurance(102.51)
                .subObjectName("policySubObject1")
                .build();
        PolicyObject policyObject1 = PolicyObject.builder()
                .objectName("policyObject1")
                .policySubObjectList(List.of(policySubObject1, policySubObject2))
                .build();
        Policy policy1 = Policy.builder()
                .policyNumber("LV00-00-000000-1")
                .policyStatus("REGISTERED")
                .policyObjectList(List.of(policyObject1))
                .build();
        PolicyPrice expectedResult = PolicyPrice.builder()
                .price(17.13)
                .currency("EUR").build();
        assertThat(premiumCalculatorService.calculate(policy1)).isEqualTo(expectedResult);
    }

    @Test
    void calculateMultiplePolicyObjectsTest(){
        PolicySubObject policySubObject1 = PolicySubObject.builder()
                .riskType("FIRE")
                .sumInsurance(500.00)
                .subObjectName("policySubObject1")
                .build();
        PolicySubObject policySubObject2 = PolicySubObject.builder()
                .riskType("THEFT")
                .sumInsurance(102.51)
                .subObjectName("policySubObject1")
                .build();
        PolicyObject policyObject1 = PolicyObject.builder()
                .objectName("policyObject1")
                .policySubObjectList(List.of(policySubObject1, policySubObject2))
                .build();
        PolicyObject policyObject2 = PolicyObject.builder()
                .objectName("policyObject2")
                .policySubObjectList(List.of(policySubObject1, policySubObject2))
                .build();
        Policy policy = Policy.builder()
                .policyNumber("LV00-00-000000-1")
                .policyStatus("REGISTERED")
                .policyObjectList(List.of(policyObject1,policyObject2))
                .build();
        PolicyPrice expectedResult = PolicyPrice.builder()
                .price(34.25)
                .currency("EUR").build();

        assertThat(premiumCalculatorService.calculate(policy)).isEqualTo(expectedResult);
    }

    @Test
    void calculateWithWrongRiskTest() {
        PolicySubObject policySubObject1 = PolicySubObject.builder()
                .riskType("FIREEEE")
                .sumInsurance(500.00)
                .subObjectName("policySubObject1")
                .build();
        PolicySubObject policySubObject2 = PolicySubObject.builder()
                .riskType("THEFT")
                .sumInsurance(102.51)
                .subObjectName("policySubObject2")
                .build();
        PolicyObject policyObject1 = PolicyObject.builder()
                .objectName("policyObject1")
                .policySubObjectList(List.of(policySubObject1, policySubObject2))
                .build();
        Policy policy1 = Policy.builder()
                .policyNumber("LV00-00-000000-1")
                .policyStatus("REGISTERED")
                .policyObjectList(List.of(policyObject1))
                .build();

        assertThrows(RiskTypeNotFoundException.class, () -> premiumCalculatorService.calculate(policy1));
    }

    @Test
    void calculateNullTest() {
        PolicySubObject policySubObject1 = PolicySubObject.builder()
                .riskType("FIRE")
                .sumInsurance(null)
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
        Policy policy1 = Policy.builder()
                .policyNumber("LV00-00-000000-1")
                .policyStatus("REGISTERED")
                .policyObjectList(List.of(policyObject1))
                .build();

        assertThrows(NullPointerException.class ,() -> {
            premiumCalculatorService.calculate(policy1);
        });
    }

}
