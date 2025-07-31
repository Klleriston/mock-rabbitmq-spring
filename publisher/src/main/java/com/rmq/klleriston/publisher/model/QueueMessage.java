package com.rmq.klleriston.publisher.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class QueueMessage {
    @JsonProperty("key1")
    private String key1;
    @JsonProperty("key2")
    private String key2;
}
