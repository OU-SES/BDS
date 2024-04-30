package com.seslab.bfsserver.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Broker{

    @Id
    private String id;
    private String ipAddress;
    private String port;
    private int cntClient;
    private int fixQuality;
    private int numSat;
    private float HDOP;
    private float altitude;
    private float height;
    private double[] location;
}