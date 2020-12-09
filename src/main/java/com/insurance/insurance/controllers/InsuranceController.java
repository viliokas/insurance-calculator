package com.insurance.insurance.controllers;

import com.insurance.insurance.models.Policy;
import com.insurance.insurance.models.response.PolicyPrice;
import com.insurance.insurance.services.PremiumCalculatorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;

@RestController
@RequestMapping("/api")
public class InsuranceController {
    Logger logger = LoggerFactory.getLogger(InsuranceController.class);
    @Autowired
    PremiumCalculatorService premiumCalculatorService;

    @PostMapping("/calculate-insurance")
    ResponseEntity<PolicyPrice> calculateInsurance(@Valid @RequestBody Policy policy) {
        logger.info("{ /calculate-insurance } API has been called");
            return ResponseEntity
                    .ok()
                    .body(premiumCalculatorService.calculate(policy));
    }
}
