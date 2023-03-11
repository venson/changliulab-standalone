package com.venson.changliulabstandalone.service.front;


import com.venson.changliulabstandalone.entity.enums.SendStatus;

public interface MsmFrontService {
    SendStatus getSecurityCode(String emailUrl);
}
