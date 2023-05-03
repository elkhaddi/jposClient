package com.example.jposgateway.utils;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TransactionRequest {
    private String processingCode;
    private String cardNumber;
    private String expiry;
    private String amount;
    private String merchantId;
}
