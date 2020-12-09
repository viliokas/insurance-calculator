package com.insurance.insurance.services;

import com.insurance.insurance.controllers.InsuranceController;
import com.insurance.insurance.enums.RiskType;
import com.insurance.insurance.models.Policy;
import com.insurance.insurance.models.response.PolicyPrice;
import com.insurance.insurance.utils.NumberFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class PremiumCalculatorService {
    Logger logger = LoggerFactory.getLogger(PremiumCalculatorService.class);
    private String roundPattern = "0.00";

    /**
     * This is the insurance price premium policy calculator.
     * @param policy Policy object.
     * @return PolicyPrice.
     * @exception ParseException On input error.
     */
    public PolicyPrice calculate(Policy policy){
        logger.info("Calculating...");
        AtomicReference<Double> sumInsuredFire = new AtomicReference<>((double) 0);
        AtomicReference<Double> sumInsuredTheft = new AtomicReference<>((double) 0);

        // Sum up each PolicySubObject SumInsurance prices.
        policy.getPolicyObjectList().stream().forEach(policyObject -> {
            policyObject.getPolicySubObjectList().stream().forEach(policySubObject -> {
                switch (RiskType.getRiskType(policySubObject.getRiskType())) {
                    case FIRE:
                        sumInsuredFire.set(sumInsuredFire.get() + policySubObject.getSumInsurance());
                        break;
                    case THEFT:
                        sumInsuredTheft.set(sumInsuredTheft.get() + policySubObject.getSumInsurance());
                        break;
                }
            });
        });

        Double fireCoefficient = RiskType.getRiskTypeCoefficient(RiskType.FIRE.name(),sumInsuredFire.get());
        Double theftCoefficient = RiskType.getRiskTypeCoefficient(RiskType.THEFT.name(),sumInsuredTheft.get());

        Double calculatedPremiumFire = sumInsuredFire.get() * fireCoefficient;
        Double calculatedPremiumTheft = sumInsuredTheft.get() * theftCoefficient;
        Double calculatedPremium = calculatedPremiumFire + calculatedPremiumTheft;

        Double roundPremiumPrice = NumberFormat.roundDoubleHalfEven(calculatedPremium,roundPattern);

        return PolicyPrice.builder()
                .price(roundPremiumPrice)
                .build();
    }
}
