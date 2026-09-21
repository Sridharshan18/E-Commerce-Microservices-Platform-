package com.ecommerce.notification.payload;


import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.function.Consumer;

@Service
@Slf4j
public class OrderEventConsumer {

//    @RabbitListener(queues = "${rabbitmq.queue.name}")
//    public void handleOrderEvent(OrderCreatedEvent orderEvent) {
//
//        System.out.println("Received Order Event: " + orderEvent);
//
//        long orderId = orderEvent.getOrderId();
//        OrderStatus orderStatus = orderEvent.getOrderStatus();
//
//        System.out.println("OrderID: " + orderId);
//        System.out.println("Received Order Status: " + orderStatus);
//
//
//        //Update Database
//        //Send notification
//        //Send emails
//    }

    @Bean
    public Consumer<OrderCreatedEvent> orderCreatedEventConsumerStreams()
    {
        return event -> {
              log.info("Received Order created Event for order: {}" , event.getOrderId());
              log.info("Received Order created Event for user: {}" , event.getUserId());
        };
    };
}
