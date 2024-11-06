package com.gthm.api.kafka;


import lombok.AllArgsConstructor;


@AllArgsConstructor
public class SdpRecord {

    public final byte[] value;
    public final String data;

}