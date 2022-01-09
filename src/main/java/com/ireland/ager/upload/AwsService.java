package com.ireland.ager.upload;

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
public class AwsService {

    @Value("${cloud.aws.s3.bucket.url}")
    private String defaultUrl;

    @Value("${cloud.aws.s3.bucket.name}") // 프로퍼티에서 cloud.aws.s3.bucket에 대한 정보를 불러옴
    public String bucket;

    private final AmazonS3Client amazonS3Client;

    public List<String> upload(List<MultipartFile> uploadFiles) throws IOException {
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
                // 파일 객체 생성
                // System.getProperty => 시스템 환경에 관한 정보를 얻을 수 있다. (user.dir = 현재 작업 디렉토리를 의미함)
                File file = new File(System.getProperty("user.dir") + saveFileName);
                // 파일 변환
                uploadFile.transferTo(file);
                // S3 파일 업로드
                uploadOnS3(saveFileName, file);
                // 주소 할당
                url = defaultUrl + saveFileName;
                // 파일 삭제
                file.delete();
                uploadUrl.add(url);
            } catch (StringIndexOutOfBoundsException e) {
                url = null;
            }

        }

        return uploadUrl;
    }

    private static String getUuid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    private void uploadOnS3(final String findName, final File file) {
        // AWS S3 전송 객체 생성
        final TransferManager transferManager = new TransferManager(this.amazonS3Client);
        // 요청 객체 생성
        final PutObjectRequest request = new PutObjectRequest(bucket, findName, file);
        // 업로드 시도
        final Upload upload =  transferManager.upload(request);

        try {
            upload.waitForCompletion();
        } catch (AmazonClientException | InterruptedException amazonClientException) {
            amazonClientException.printStackTrace();
        }
    }
}
