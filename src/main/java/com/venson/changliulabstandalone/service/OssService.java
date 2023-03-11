package com.venson.changliulabstandalone.service;

import com.venson.changliulabstandalone.entity.OssAuth;
import org.springframework.web.multipart.MultipartFile;

public interface OssService {
    String uploadFileAvatar(String path, MultipartFile file);


    OssAuth getUploadAuth(String dir);
}
