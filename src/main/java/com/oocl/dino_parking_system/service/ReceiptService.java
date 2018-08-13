package com.oocl.dino_parking_system.service;

import com.oocl.dino_parking_system.entitie.Receipt;
import org.springframework.stereotype.Component;

@Component
public class ReceiptService {
    public Receipt generateReceipt() {
        return new Receipt();
    }
}
