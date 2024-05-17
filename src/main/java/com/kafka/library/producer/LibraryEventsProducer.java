package com.kafka.library.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kafka.library.domain.LibraryEvent;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import lombok.extern.log4j.Log4j2;

@Component
@Log4j2
public class LibraryEventsProducer {

  @Value("${spring.kafka.topic}")
  private String topicName;

  private final KafkaTemplate<Integer, String> kafkaTemplate;
  private ObjectMapper objectMapper;

  public LibraryEventsProducer(KafkaTemplate<Integer, String> kafkaTemplate, ObjectMapper objectMapper){
    this.kafkaTemplate=kafkaTemplate;
    this.objectMapper=objectMapper;
  }

  public CompletableFuture<SendResult<Integer, String>> sendEvent(LibraryEvent libraryEvent) throws JsonProcessingException {

    return kafkaTemplate
            .send(getProducerRecord(topicName, libraryEvent.libraryEventId(), this.objectMapper.writeValueAsString(libraryEvent)))
            .whenComplete((integerStringSendResult, throwable) -> {
                if(throwable!=null){
                  log.error("Error occurred while sending the event: "+ throwable.getMessage());
                  throwable.printStackTrace();
                }else{
                  log.info("Library event sent successfully with data: "+integerStringSendResult.toString());
                }
            });
  }

  private ProducerRecord<Integer, String> getProducerRecord(String topicName, Integer key, String value){
    List<Header> headers= List.of(new RecordHeader("event-source", "Scanner".getBytes()));
    return new ProducerRecord<>(topicName, null, key, value, headers);
  }
}
