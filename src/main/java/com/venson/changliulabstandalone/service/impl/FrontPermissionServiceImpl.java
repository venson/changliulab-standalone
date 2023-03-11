package com.venson.changliulabstandalone.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.venson.changliulabstandalone.entity.FrontPermission;
import com.venson.changliulabstandalone.mapper.FrontPermissionMapper;
import com.venson.changliulabstandalone.service.front.FrontPermissionService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FrontPermissionServiceImpl extends ServiceImpl<FrontPermissionMapper, FrontPermission> implements FrontPermissionService {
    @Override
    public List<String> selectPermissionValueByUserId(Long id) {
        List<String> empty = new ArrayList<>();
        empty.add("user");
        // TODO fullfill detail permission
        return empty;
    }
}
