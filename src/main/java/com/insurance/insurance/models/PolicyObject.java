package com.insurance.insurance.models;
import lombok.Builder;
import lombok.Getter;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Builder
public class PolicyObject {
    @NotNull(message="objectName cannot be null")
    String objectName;
    @NotNull(message="PolicySubObject list cannot be null")
    List<@Valid PolicySubObject> policySubObjectList;
}
