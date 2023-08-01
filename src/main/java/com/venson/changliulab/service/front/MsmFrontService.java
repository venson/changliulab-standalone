package com.venson.changliulab.service.front;


import com.venson.changliulab.entity.enums.SendStatus;

public interface MsmFrontService {
    SendStatus getSecurityCode(String emailUrl);
}
