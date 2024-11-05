package com.gthm.api.kafka;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;


@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SdpRecord {

    public byte[] value;
    public String data;

}