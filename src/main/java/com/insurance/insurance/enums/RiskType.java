package com.insurance.insurance.enums;

import com.insurance.insurance.exceptions.RiskTypeNotFoundException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum RiskType {

    FIRE(0.014, 100, 0.024),
    THEFT(0.11, 14, 0.05);

    private double defaultCoefficient;
    private double breakpoint;
    private double afterBreakpointCoefficient;

    public static RiskType getRiskType(String riskType) {
        return Arrays.stream(RiskType.values()).filter(
                eAction -> eAction.name().equalsIgnoreCase(riskType)
        ).findFirst().orElseThrow(() -> new RiskTypeNotFoundException(riskType));
    }

    public static double getRiskTypeCoefficient(String riskType, double sumInsured) {
        RiskType riskTypeEnum = getRiskType(riskType);
        return (sumInsured > riskTypeEnum.getBreakpoint()) ? riskTypeEnum.getAfterBreakpointCoefficient() : riskTypeEnum.getDefaultCoefficient();
    }
}