package com.venson.changliulab.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.MatchMode;
import com.aliyun.oss.model.PolicyConditions;
import com.venson.changliulab.entity.OssAuth;
import com.venson.changliulab.service.OssService;
import com.venson.changliulab.utils.OssUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
@Slf4j
public class OssServiceImpl implements OssService {

    @Autowired
    private OSS ossClient;

    @Value("${alibaba.cloud.oss.endpoint}")
    private String endpoint;
    @Value("${alibaba.cloud.bucket-name}")
    private String bucketName;
    @Value("${alibaba.cloud.access-key}")
    private String accessId;

    @Override
    public String uploadFileAvatar(String path, MultipartFile file) {
        String fileName = file.getOriginalFilename();
//        fileName = URLEncoder.encode(fileName,StandardCharsets.UTF_8);
        String fullPath = path + "/" + fileName;
//        OSS ossClient = new OSSClientBuilder().build(OssUtils.END_POINT, OssUtils.ACCESS_KEY_ID, OssUtils.ACCESS_KEY_SECRET);

        try {
            InputStream inputStream = file.getInputStream();
            log.info("try upload file:" + fullPath);
            ossClient.putObject(OssUtils.BUCKET_NAME, fullPath, inputStream);
//            String manualUrl = "https://" + OssUtils.BUCKET_NAME + "." + OssUtils.END_POINT + "/" + fullPath;

            URL url = ossClient.generatePresignedUrl(OssUtils.BUCKET_NAME, fullPath,
                    new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7));
            ossClient.shutdown();

            String s;
            String urlString = url.getProtocol()
                    + ':'
                    + ((s = url.getAuthority()) != null && !s.isEmpty()
                    ? "//" + s : "")
                    + ((s = url.getPath()) != null ? s : "");
            log.info("successful uploaded file:" + urlString);

            return urlString;
        } catch (IOException e) {
            e.printStackTrace();
            log.error("failed to upload file:" + path + "/" + file.getOriginalFilename());
        } finally {
            if (ossClient != null)
                ossClient.shutdown();
        }
        return null;
    }

    @Override
    public OssAuth getUploadAuth(String dir) {
        if(!StringUtils.hasText(dir)){
            log.info("empty dir");
        }
        try {
            long expireTime = 10;
            long expireEndTime = System.currentTimeMillis() + expireTime * 1000;
            Date expiration = new Date(expireEndTime);
//            log.info("current"+System.currentTimeMillis());
//            log.info("expireTime" +expireEndTime);
            PolicyConditions policyConds = new PolicyConditions();
            policyConds.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, 1048576000);
            policyConds.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, dir);

            String postPolicy = ossClient.generatePostPolicy(expiration, policyConds);
            byte[] binaryData = postPolicy.getBytes(StandardCharsets.UTF_8);
            String encodedPolicy = BinaryUtil.toBase64String(binaryData);
            String postSignature = ossClient.calculatePostSignature(postPolicy);
            String host = "https://" + bucketName +"." + endpoint;
            OssAuth auth = new OssAuth();
            auth.setHost(host);
            auth.setAccessId(accessId);
            auth.setExpire(String.valueOf(expireEndTime/1000));
            auth.setPolicy(encodedPolicy);
            auth.setSignature(postSignature);
            auth.setDir(dir);
            return auth;
        } catch (Exception e) {
            log.info(e.getMessage());
            return null;
        }
    }
}
