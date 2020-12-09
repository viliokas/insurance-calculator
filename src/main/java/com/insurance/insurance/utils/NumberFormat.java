package com.insurance.insurance.utils;

import java.text.DecimalFormat;
import java.text.ParseException;

public class NumberFormat {

    public static Double roundDoubleHalfEven(Double number, String pattern) {
        DecimalFormat df = new DecimalFormat(pattern);
        String roundPremiumPrice = df.format(number);
        Double formattedPremiumPrice = null;
        try {
            formattedPremiumPrice = (Double) df.parse(roundPremiumPrice);
        } catch (
                ParseException e) {
            e.printStackTrace();
        }
        return formattedPremiumPrice;
    }
}
