package com.example.jposgateway.utils;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.springframework.stereotype.Component;

@Component
public class JsonToIsoConverter {
    public ISOMsg convertJsonToIso(TransactionRequest transactionRequest) throws ISOException {
        // penser a utiliser un Builder  ISO8583MsgBuilder builder = new ISO8583MsgBuilder();
        ISOMsg isoMsg = new ISOMsg();
        isoMsg.setMTI("1200");
        isoMsg.set(2,transactionRequest.getCardNumber());
        isoMsg.set(3,transactionRequest.getProcessingCode());
        isoMsg.set(4,transactionRequest.getAmount());
        isoMsg.set(7,transactionRequest.getExpiry());
        isoMsg.set(32,transactionRequest.getMerchantId());
        return isoMsg;
    }
}
