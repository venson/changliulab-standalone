package com.venson.changliulabstandalone.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OssResult implements Serializable {
    @Serial
    private static final long serialVersionUID=234234349L;
    private String address;
}
