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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
<<<<<<< HEAD
@Slf4j
=======
@Transactional
>>>>>>> 3894f6a44c4f3bdd644616d78734504f1b83f139
@RequiredArgsConstructor
public class UploadServiceImpl {
    @Value("${cloud.aws.s3.bucket.url}")
    private String defaultUrl;

    @Value("${cloud.aws.s3.bucket.name}") // 프로퍼티에서 cloud.aws.s3.bucket에 대한 정보를 불러옴
    public String bucket;

    private final AmazonS3Client amazonS3Client;

    /**
     * @Method Name : deleteS3file
     * @작성자 : Potter
     * @Method 설명 : s3의 파일을 삭제 (버킷명, 삭제하고 싶은 폴더나 파일명 ex) test/test.png
     */

    public void delete(){
        amazonS3Client.deleteObject(bucket,"3f7739da66d54d5e8de5174d317cc0e7.png");
    }


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
            String url;
            try {
                // 확장자를 찾기 위한 코드
                final String ext = origName.substring(origName.lastIndexOf('.'));
                log.info("확장자는 {} ",ext);

                if(ext.equals(".jpg") || ext.equals(".png")||ext.equals(".jpeg")) {
                    log.info("파일 확장자 확인 완료");
                    // 파일이름 암호화
                    final String saveFileName = getUuid() + ext;
                    File file = new File(System.getProperty("user.dir") + saveFileName);
                    uploadFile.transferTo(file);
                    uploadOnS3(saveFileName, file);
                    url = defaultUrl + saveFileName;
                    file.delete();
                    uploadUrl.add(url);
                    return uploadUrl;
                }
            } catch (StringIndexOutOfBoundsException | IOException e) {
                url = null;
            }
        }
        log.info("파일 확장자 확인 실패");
        log.info("uploadUrl : {}",uploadUrl);
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