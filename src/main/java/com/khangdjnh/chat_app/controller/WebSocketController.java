package com.khangdjnh.chat_app.controller;


import com.khangdjnh.chat_app.kafka.ChatKafkaProducer;
import com.khangdjnh.chat_app.model.ChatMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebSocketController {

    @Autowired
    private ChatKafkaProducer chatKafkaProducer;

    @MessageMapping("/chat")
    public void receiveMessage(ChatMessage message) {
        chatKafkaProducer.send(message); // Gửi Kafka
    }
}
