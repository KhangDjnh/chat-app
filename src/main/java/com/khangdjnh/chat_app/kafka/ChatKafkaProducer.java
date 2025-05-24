package com.khangdjnh.chat_app.kafka;

import com.khangdjnh.chat_app.model.ChatMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class ChatKafkaProducer {
    @Autowired
    private KafkaTemplate<String, ChatMessage> kafkaTemplate;

    public void send(ChatMessage message) {
        kafkaTemplate.send("chat-topic", message);
    }
}
