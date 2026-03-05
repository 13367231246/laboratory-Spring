package com.hss.controller;

import com.hss.pojo.Result;
import com.hss.utils.AliOssUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
public class FileUploadController {

    @PostMapping("/upload")
    public Result<String> upload(MultipartFile file) throws Exception {
        //获取原始文件名
        String originalFilrname = file.getOriginalFilename();
        //保证文件的名字是唯一的，从而防止文件覆盖，此时就可借助uuid
        //先得到随机UUID字符串，再拼上这个文件的后缀名(从最后面.的位置截取)
        String filename = UUID.randomUUID().toString()+originalFilrname.substring(originalFilrname.lastIndexOf("."));
        //file.transferTo(new File("C:\\Users\\贺莎莎\\Desktop\\files\\"+filename));
        String url = AliOssUtil.upLoadFile(filename, file.getInputStream());
        return Result.success(url);

    }
}
