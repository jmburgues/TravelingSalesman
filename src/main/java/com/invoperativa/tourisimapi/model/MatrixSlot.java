package com.invoperativa.tourisimapi.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class MatrixSlot {
    private String name;
    private Integer column;
    private Integer row;
    private Integer distance;
    private Integer originalDistance;
    private Integer pivot;
}
