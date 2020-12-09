package com.insurance.insurance.exceptions;

public class RiskTypeNotFoundException extends RuntimeException {
    public RiskTypeNotFoundException(String riskType) {
        super("Risk type " + riskType + " not found");
    }
}

