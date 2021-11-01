package com.invoperativa.tourisimapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class Slot {
    private String origin;
    private String destination;
    private Integer row;
    private Integer column;
    private Integer distance;
    private Integer originalDistance;
    private Integer pivot;
}
