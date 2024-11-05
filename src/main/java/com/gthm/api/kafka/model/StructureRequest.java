package com.gthm.api.kafka.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StructureRequest {

    private int id;
    private String name;
    private String type;
    private List<Review> reviews;


}