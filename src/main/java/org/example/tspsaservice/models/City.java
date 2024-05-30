package org.example.tspsaservice.models;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Document(value = "cities")
@Data
@Builder

public class City {
    @Id
    private String id;
    private String name;
    private Map<String, Double> distances;
}
