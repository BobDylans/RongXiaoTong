package com.gaoge.controller;

import com.gaoge.common.Result;
import com.gaoge.common.StatusCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Properties;
import java.util.UUID;

/**
 * 图片文件上传
 */
@Api(value = "文件")
@RestController
@RequestMapping("/file")
public class FilesUploadController {
    @Value("${file.location.directory}")
    private String fileDirectory;

    //    文件上传
    @ApiOperation(value = "文件上传")
    @PostMapping("/upload/{type}")
    public Result<String> upload(@PathVariable("type") String type,
                                 @RequestParam("file") MultipartFile file) throws Exception {
        String originalFilename = file.getOriginalFilename();
        String tail = originalFilename.substring(originalFilename.lastIndexOf("."));
        if (tail.equals("java") || tail.equals("class") || tail.equals("php")) {
            throw new Exception("请传入正确格式文件");
        }
//        创建目录
        String header = UUID.randomUUID().toString().replaceAll("-", "");
        String newFileName = header + tail;
        String targetFileLocation = fileDirectory +File.separator+ type;
        File file1 = new File(targetFileLocation);
        if (!file1.exists()) {
            file1.mkdirs();
        }
        //创建文件
        String targetFileName = targetFileLocation + File.separator + newFileName;
        File targetFile = new File(targetFileName);
        if (!targetFile.exists()) {
            targetFile.createNewFile();
        }
        file.transferTo(targetFile);
        return new Result<String>(true, StatusCode.OK,"上传成功",newFileName);

    }


}
