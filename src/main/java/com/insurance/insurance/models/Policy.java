package com.insurance.insurance.models;
import lombok.Builder;
import lombok.Getter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Builder
public class Policy {
    @NotNull(message="policyNumber cannot be null")
    String policyNumber;
    @NotNull(message="policyStatus cannot be null")
    String policyStatus;
    @NotNull(message="policyObjectList list cannot be null")
    List<@Valid PolicyObject> policyObjectList;
}
