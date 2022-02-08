package com.ireland.ager.product.service;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.Upload;
import com.ireland.ager.board.entity.BoardUrl;
import com.ireland.ager.product.entity.Url;
import com.ireland.ager.product.exception.InvaildFileExtensionException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
     * currentFileImageUrlList은 다운로드용 url입니다 삭제하고 싶은 파일 명을 입력값으로 넣어줘야합니다.
     */

    public void delete(List<Url> currentFileImageUrlList,String thumbNailUrl) throws AmazonServiceException {
        try {
            amazonS3Client.deleteObject(bucket, thumbNailUrl.split("/")[3]);
            log.info("삭제될 파일의 이름은 : {}",thumbNailUrl.split("/")[3]);
        }
        catch (AmazonServiceException e){
            e.printStackTrace();
        }
        for (Url url : currentFileImageUrlList) {

            amazonS3Client.deleteObject(bucket, url.getUrl().split("/")[3]);
            log.info("삭제될 파일의 이름은 : {}",url.getUrl().split("/")[3]);
        }
    }

    public void deleteBoard(List<BoardUrl> currentFileImageUrlList, String thumbNailUrl) throws AmazonServiceException {
        try {
            amazonS3Client.deleteObject(bucket, thumbNailUrl.split("/")[3]);
            log.info("삭제될 파일의 이름은 : {}",thumbNailUrl.split("/")[3]);
        }
        catch (AmazonServiceException e){
            e.printStackTrace();
        }
        for (BoardUrl url : currentFileImageUrlList) {

            amazonS3Client.deleteObject(bucket, url.getUrl().split("/")[3]);
            log.info("삭제될 파일의 이름은 : {}",url.getUrl().split("/")[3]);
        }
    }



    public List<String> uploadImages(List<MultipartFile> uploadFiles) throws IOException {
        /*
            @Method: uploadImages
            @Author: potter,frank
            @Param: uploadFiles
            @content: 이미지 파일들을 s3에 업로드하고 url을 return
        */
        log.info("업로드 파일의 갯수 : {}", uploadFiles.size());
        //첫번쨰 장을 썸네일 로 만든다.
        MultipartFile thumbFile=uploadFiles.get(0);
        List<String> uploadUrl = new ArrayList<>();
        for (MultipartFile uploadFile : uploadFiles) {
            log.info("파일 확인용 : {}: ",uploadFile.getOriginalFilename());
            String origName = uploadFile.getOriginalFilename();
            String url;
            ObjectMetadata objectMetadata=new ObjectMetadata();
            objectMetadata.setContentLength(uploadFile.getSize());
            objectMetadata.setContentType(uploadFile.getContentType());
            try(InputStream inputStream=uploadFile.getInputStream()) {
                // 확장자를 찾기 위한 코드
                final String ext = origName.substring(origName.lastIndexOf('.'));
                // 파일이름 암호화
                final String saveFileName = getUuid() + ext;
                //파일을 저장하는 방식 파일저장후 업로드후 파일 삭제
//                File file = new File(System.getProperty("user.dir") + saveFileName);
//                uploadFile.transferTo(file);
                //FIXME 업로드 부분 바뀐 방식 inputStream으로 파일을 저장 안하고 네트워크에서 네트워크로 스무스하게 넘어가는 바이브로 변경
                uploadOnS3(saveFileName, inputStream,objectMetadata);
                url = defaultUrl + saveFileName;
                uploadUrl.add(url);
                //file.delete();
            } catch (StringIndexOutOfBoundsException | IOException e) {
                url = null;
            }
        }
        return uploadUrl;
    }

    public String makeThumbNail(MultipartFile multipartFile) throws IOException {
        String origName=multipartFile.getOriginalFilename();
        final String ext = origName.substring(origName.lastIndexOf('.'));
        final String saveFileName = "Thumbnail_"+getUuid() + ext;
        BufferedImage image = ImageIO.read(multipartFile.getInputStream());
        double getWidth = image.getWidth();
        double getHeight = image.getHeight();
        double resizeRatio = getWidth / getHeight;
        int mediumHeight = 100;
        int mediumWidth = (int) (resizeRatio * mediumHeight);
        BufferedImage thumbnail_medium = Thumbnails.of(image).size(mediumWidth,mediumHeight).asBufferedImage();
       return uploadThumbNailToS3(thumbnail_medium,saveFileName,ext);
    }
    public String uploadThumbNailToS3(BufferedImage image,String Filename,String ext)
            throws IllegalStateException, IOException {
        String url;
        try {
            // outputstream에 image객체를 저장
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            log.info("확장자는 :{}",ext.substring(1));
            ImageIO.write(image, ext.substring(1), os);
            //byte[]로 변환
            byte[] bytes = os.toByteArray();
            //metadata 설정
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(bytes.length);
            objectMetadata.setContentType(ext.substring(1));
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
            final TransferManager transferManager = new TransferManager(this.amazonS3Client);
            final PutObjectRequest request = new PutObjectRequest(bucket, Filename, byteArrayInputStream,objectMetadata);
            final Upload upload = transferManager.upload(request);
                upload.waitForCompletion();
                //Todo 아마존 sdk를 이용하여서 url가져오는 방법  통신을 하는 과정이 추가 되므로 안쓰려고 한다.
//                return amazonS3Client.getUrl(bucket,Filename).toString();
        } catch (AmazonServiceException | InterruptedException e) {
            e.printStackTrace();
        }
        url=defaultUrl+Filename;
        return url;
    }

    private static String getUuid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    private void uploadOnS3(final String fileName, final InputStream inputStream,final  ObjectMetadata objectMetadata) {
        final TransferManager transferManager = new TransferManager(this.amazonS3Client);
        final PutObjectRequest request = new PutObjectRequest(bucket, fileName, inputStream,objectMetadata);
        final Upload upload = transferManager.upload(request);
        try {
            upload.waitForCompletion();
        } catch (AmazonClientException | InterruptedException amazonClientException) {
            log.info("getCause : {}", amazonClientException.getCause());
            throw new InvaildFileExtensionException();
        }
    }
}