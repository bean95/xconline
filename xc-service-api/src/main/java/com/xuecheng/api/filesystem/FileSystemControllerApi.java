package com.xuecheng.api.filesystem;

import com.xuecheng.framework.domain.filesystem.response.UploadFileResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.multipart.MultipartFile;

@Api(value = "文件管理接口",description = "文件管理接口")
public interface FileSystemControllerApi {

    @ApiOperation("上传文件")
    UploadFileResult upload(MultipartFile multipartFile,String businessKey,String fileTag,String metadata);

}
