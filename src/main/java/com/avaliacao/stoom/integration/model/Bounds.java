package com.avaliacao.stoom.integration.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Bounds {

    private Northeast northeast;

    private Southwest southwest;
}