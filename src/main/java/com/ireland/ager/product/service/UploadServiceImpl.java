package com.ireland.ager.product.service;

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

    public void delete(List<Url> currentFileImageUrlList, String thumbNailUrl) throws AmazonServiceException {
        try {
            amazonS3Client.deleteObject(bucket, thumbNailUrl.split("/")[3]);
            log.info("삭제될 파일의 이름은 : {}", thumbNailUrl.split("/")[3]);
        } catch (AmazonServiceException e) {
            e.printStackTrace();
        }
        for (Url url : currentFileImageUrlList) {
            amazonS3Client.deleteObject(bucket, url.getUrl().split("/")[3]);
            log.info("삭제될 파일의 이름은 : {}", url.getUrl().split("/")[3]);
        }
    }

    public void deleteBoard(List<BoardUrl> currentFileImageUrlList) throws AmazonServiceException {
        for (BoardUrl url : currentFileImageUrlList) {
            amazonS3Client.deleteObject(bucket, url.getUrl().split("/")[3]);
            log.info("삭제될 파일의 이름은 : {}", url.getUrl().split("/")[3]);
        }
    }

    public List<String> uploadImages(List<MultipartFile> uploadFiles) throws IOException {
        log.info("업로드 파일의 갯수 : {}", uploadFiles.size());
        List<String> uploadUrl = new ArrayList<>();
        for (MultipartFile uploadFile : uploadFiles) {
            uploadImg(uploadFile);
        }
        return uploadUrl;
    }

    public String uploadImg(MultipartFile multipartFile) throws IOException {
        String origName = multipartFile.getOriginalFilename();
        final String ext = origName.substring(origName.lastIndexOf('.'));
        if (ext.equals(".jpg") || ext.equals(".png") || ext.equals(".jpeg")) {
            final String saveFileName = "profileImg_" + getUuid() + ext;
            BufferedImage image = ImageIO.read(multipartFile.getInputStream());
            return uploadImgToS3(image, saveFileName, ext);
        } else throw new InvaildFileExtensionException();
    }

    public String uploadImgToS3(BufferedImage image, String Filename, String ext)
            throws IllegalStateException, IOException {
        String url;
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            log.info("확장자는 :{}", ext.substring(1));
            ImageIO.write(image, ext.substring(1), os);
            byte[] bytes = os.toByteArray();
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(bytes.length);
            objectMetadata.setContentType(ext.substring(1));
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
            final TransferManager transferManager = new TransferManager(this.amazonS3Client);
            final PutObjectRequest request = new PutObjectRequest(bucket, Filename, byteArrayInputStream, objectMetadata);
            final Upload upload = transferManager.upload(request);
            upload.waitForCompletion();
        } catch (AmazonServiceException | InterruptedException e) {
            e.printStackTrace();
        }
        url = defaultUrl + Filename;
        return url;
    }

    public String makeThumbNail(MultipartFile multipartFile) throws IOException {
        String origName = multipartFile.getOriginalFilename();
        final String ext = origName.substring(origName.lastIndexOf('.'));
        if (ext.equals(".jpg") || ext.equals(".png") || ext.equals(".jpeg")) {
            final String saveFileName = "Thumbnail_" + getUuid() + ext;
            BufferedImage image = ImageIO.read(multipartFile.getInputStream());
            double getWidth = image.getWidth();
            double getHeight = image.getHeight();
            double resizeRatio = getWidth / getHeight;
            int mediumHeight = 100;
            int mediumWidth = (int) (resizeRatio * mediumHeight);
            BufferedImage thumbnail_medium = Thumbnails.of(image).size(mediumWidth, mediumHeight).asBufferedImage();
            return uploadImgToS3(thumbnail_medium, saveFileName, ext);
        } else throw new InvaildFileExtensionException();
    }

    private static String getUuid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}