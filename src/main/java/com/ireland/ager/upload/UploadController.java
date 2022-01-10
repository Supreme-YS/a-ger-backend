package com.ireland.ager.upload;

import com.ireland.ager.account.service.AuthServiceImpl;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Slf4j
@RequiredArgsConstructor
public class UploadController {
    private  final AuthServiceImpl authService;
    private final AwsService awsservice;
    @PostMapping("/upload")
    public ResponseEntity uploadFile(
            @RequestPart(value = "file") List<MultipartFile> multipartFile)throws  Exception{
        List<String> upload_url=awsservice.upload(multipartFile);
        return new ResponseEntity<>(upload_url,HttpStatus.OK);
    }
    @GetMapping("/delete")
    public ResponseEntity deleteFile(){
        awsservice.delete();
        return new ResponseEntity<>("삭제 성공",HttpStatus.OK);
    }
}
