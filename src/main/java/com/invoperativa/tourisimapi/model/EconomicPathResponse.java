package com.invoperativa.tourisimapi.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class EconomicPathResponse {
    private List<String> locations;
    private Integer totalDistance;
}
