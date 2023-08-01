package com.venson.changliulab.controller.admin;

import com.venson.changliulab.entity.OssAuth;
import com.venson.changliulab.entity.OssResult;
import com.venson.changliulab.entity.UserContextInfoBO;
import com.venson.changliulab.service.OssService;
import com.venson.changliulab.utils.ContextUtils;
import com.venson.changliulab.utils.ResUtils;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class OssController {

    private final OssService ossService;


    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<OssResult> uploadOssFile(@RequestParam String path, @RequestPart() MultipartFile file){
        String url = ossService.uploadFileAvatar(path,file);
       return ResponseEntity.ok(new OssResult(url));
    }
    @GetMapping("auth")
    @PreAuthorize("hasAuthority('Markdown.EDIT')")
    public ResponseEntity<OssAuth> getUploadAuth(@RequestParam(required = false) String path){
        UserContextInfoBO userContext = ContextUtils.getUserContext();
        if(userContext ==null || userContext.getId() ==null){
            return ResUtils.unAuthorized();
        }
        OssAuth auth = ossService.getUploadAuth(path);
        return ResponseEntity.ok(auth);
    }
}
