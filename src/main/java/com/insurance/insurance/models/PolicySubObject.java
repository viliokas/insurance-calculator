package com.insurance.insurance.models;
import lombok.Builder;
import lombok.Getter;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Builder
public class PolicySubObject {
    @NotNull(message="subObjectName cannot be null")
    String subObjectName;
    @NotNull(message="sumInsurance cannot be null")
    @Min(value = 0, message = "sumInsurance cannot be less then 0")
    Double sumInsurance;
    @NotNull(message="riskType cannot be null")
    String riskType;
}
