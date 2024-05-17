package com.kafka.library.domain;

public record LibraryEvent(Integer libraryEventId, LibraryEventType libraryEventType, Book book ) {
}
