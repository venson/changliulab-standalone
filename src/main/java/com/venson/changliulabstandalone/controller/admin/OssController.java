package com.venson.changliulabstandalone.controller.admin;

import com.venson.changliulabstandalone.entity.OssAuth;
import com.venson.changliulabstandalone.entity.OssResult;
import com.venson.changliulabstandalone.entity.UserContextInfoBO;
import com.venson.changliulabstandalone.service.OssService;
import com.venson.changliulabstandalone.utils.ContextUtils;
import com.venson.changliulabstandalone.utils.ResUtils;
import com.venson.changliulabstandalone.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/eduoss/admin/fileoss")
@Slf4j
public class OssController {

    @Autowired
    private OssService ossService;


    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<OssResult> uploadOssFile(@RequestParam String path, @RequestPart() MultipartFile file){
        String url = ossService.uploadFileAvatar(path,file);
       return ResUtils.ok(new OssResult(url));
    }
    @GetMapping("auth")
    @PreAuthorize("hasAuthority('Markdown.EDIT')")
    public ResponseEntity<OssAuth> getUploadAuth(@RequestParam(required = false) String path){
        UserContextInfoBO userContext = ContextUtils.getUserContext();
        if(userContext ==null || userContext.getId() ==null){
            return ResUtils.unAuthorized();
        }
        OssAuth auth = ossService.getUploadAuth(path);
        return ResUtils.ok(auth);
    }
}
