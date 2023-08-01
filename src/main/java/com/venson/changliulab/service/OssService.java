package com.venson.changliulab.service;

import com.venson.changliulab.entity.OssAuth;
import org.springframework.web.multipart.MultipartFile;

public interface OssService {
    String uploadFileAvatar(String path, MultipartFile file);


    OssAuth getUploadAuth(String dir);
}
