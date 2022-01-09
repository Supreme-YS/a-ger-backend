package com.ireland.ager.upload;

import com.ireland.ager.account.dto.response.AccountRes;
import com.ireland.ager.account.service.AuthServiceImpl;
import com.sun.istack.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Slf4j
@RequiredArgsConstructor
public class UploadController {
    private  final AuthServiceImpl authService;
    private final AwsService awsservice;
    @PostMapping("/upload")
    public ResponseEntity uploadFile(
            @RequestPart(value = "file") MultipartFile multipartFile)throws  Exception{

      /*  int vaildTokenStatusValue = authService.isValidToken(accessToken);*/

       /* if (vaildTokenStatusValue == 200) {
            log.info("파일 이름 확인 : {}", multipartFile.getName());
            try {
                String uploadurl = awsservice.upload(multipartFile);
                return new ResponseEntity<>(uploadurl, HttpStatus.OK);
            } catch (Exception e) {
                e.printStackTrace();
                return new ResponseEntity<>("Error",HttpStatus.BAD_REQUEST);
            }
        } else if (vaildTokenStatusValue == 401) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }*/
        log.info("파일 이름 확인 : {}", multipartFile.getName());
        String upload_url=awsservice.upload(multipartFile);
        log.info("파일 저장 성공 URL : {}",upload_url);
        return new ResponseEntity<>(upload_url,HttpStatus.OK);

    }
}
