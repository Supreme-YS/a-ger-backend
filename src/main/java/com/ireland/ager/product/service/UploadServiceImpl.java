package com.ireland.ager.product.service;

import com.amazonaws.AmazonClientException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.Upload;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class UploadServiceImpl {
    @Value("${cloud.aws.s3.bucket.url}")
    private String defaultUrl;

    @Value("${cloud.aws.s3.bucket.name}") // 프로퍼티에서 cloud.aws.s3.bucket에 대한 정보를 불러옴
    public String bucket;

    private final AmazonS3Client amazonS3Client;

    /**
     * @param currentFileImageUrlList
     * @Method Name : deleteS3file
     * @작성자 : Potter
     * @Method 설명 : s3의 파일을 삭제 (버킷명, 삭제하고 싶은 폴더나 파일명 ex) test/test.png
     *  currentFileImageUrlList은 다운로드용 url입니다 삭제하고 싶은 파일 명을 입력값으로 넣어줘야합니다.
     */

    public void delete(List<String> currentFileImageUrlList) {
        for(String url: currentFileImageUrlList) {
            amazonS3Client.deleteObject(bucket,url.split("/")[3]);
        }
    }

    public List<String> uploadImages(List<MultipartFile> uploadFiles) {
        /*
            @Method: uploadImages
            @Author: potter,frank
            @Param: uploadFiles
            @content: 이미지 파일들을 s3에 업로드하고 url을 return
        */

        List<String> uploadUrl = new ArrayList<>();

        for (MultipartFile uploadFile : uploadFiles) {
            String origName = uploadFile.getOriginalFilename();
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
        final Upload upload = transferManager.upload(request);
        try {
            upload.waitForCompletion();
        } catch (AmazonClientException | InterruptedException amazonClientException) {
            amazonClientException.printStackTrace();
        }
    }
}