package com.invoperativa.tourisimapi.model;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class Location {
    private int id;
    private String name;
    private Map<Integer,Integer> distances;
}
