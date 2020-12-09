package com.insurance.insurance.models.response;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;

@Builder
@Getter
@EqualsAndHashCode
public class PolicyPrice {
    @NonNull
    Double price;
    @Builder.Default
    String currency = "EUR";
}
