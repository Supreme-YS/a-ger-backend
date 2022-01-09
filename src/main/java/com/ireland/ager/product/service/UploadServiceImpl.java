package com.ireland.ager.product.service;

import com.amazonaws.AmazonClientException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.Upload;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
@Service
@RequiredArgsConstructor
public class UploadServiceImpl {
    @Value("${cloud.aws.s3.bucket.url}")
    private String defaultUrl;

    @Value("${cloud.aws.s3.bucket.name}") // 프로퍼티에서 cloud.aws.s3.bucket에 대한 정보를 불러옴
    public String bucket;

    private final AmazonS3Client amazonS3Client;

    public List<String> uploadImages(List<MultipartFile> uploadFiles) {
        /*
            @Method: uploadImages
            @Author: potter,frank
            @Param: uploadFiles
            @content: 이미지 파일들을 s3에 업로드하고 url을 return
        */
        List<String> uploadUrl=new ArrayList<>();
        for(MultipartFile uploadFile: uploadFiles) {
            String origName = uploadFile.getOriginalFilename();
            ByteArrayOutputStream jpegOutputStream = new ByteArrayOutputStream();
            String url;
            try {
                // 확장자를 찾기 위한 코드
                final String ext = origName.substring(origName.lastIndexOf('.'));
                // 파일이름 암호화
                final String saveFileName = getUuid() + ext;
                File file = new File(System.getProperty("user.dir") + saveFileName);
                uploadFile.transferTo(file);
                uploadOnS3(saveFileName, file);
                url = defaultUrl + saveFileName;
                file.delete();
                uploadUrl.add(url);
            } catch (StringIndexOutOfBoundsException | IOException e) {
                url = null;
            }
        }
        return uploadUrl;
    }
    private static String getUuid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    private void uploadOnS3(final String findName, final File file) {
        final TransferManager transferManager = new TransferManager(this.amazonS3Client);
        final PutObjectRequest request = new PutObjectRequest(bucket, findName, file);
        final Upload upload =  transferManager.upload(request);
        try {
            upload.waitForCompletion();
        } catch (AmazonClientException | InterruptedException amazonClientException) {
            amazonClientException.printStackTrace();
        }
    }
}
