package com.challenge.orders.service.domain.orders.usecases;

import com.challenge.orders.service.domain.orders.models.Order;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class CreateOrderUseCase {

    private final Logger logger = LogManager.getLogger(this.getClass());


    public String execute(Order order) {
        return null;
    }
}
