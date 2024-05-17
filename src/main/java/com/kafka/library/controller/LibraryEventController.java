package com.kafka.library.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kafka.library.domain.LibraryEvent;
import com.kafka.library.producer.LibraryEventsProducer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.log4j.Log4j2;

@RestController
@Log4j2
public class LibraryEventController {

  @Autowired
  private LibraryEventsProducer libraryEventsProducer;

  @PostMapping("/v1/libraryevent")
  public ResponseEntity<LibraryEvent> postLibraryEvent(@RequestBody LibraryEvent libraryEvent) throws JsonProcessingException {
    log.info("event received: "+libraryEvent);
    libraryEventsProducer.sendEvent(libraryEvent);
    return ResponseEntity.status(HttpStatus.CREATED).body(libraryEvent);
  }
}
