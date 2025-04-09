package com.cmq.cloudnestbackend.controller;

import com.cmq.cloudnestbackend.annotation.AuthCheck;
import com.cmq.cloudnestbackend.common.BaseResponse;
import com.cmq.cloudnestbackend.common.ResultUtils;
import com.cmq.cloudnestbackend.constant.UserConstant;
import com.cmq.cloudnestbackend.exception.BusinessException;
import com.cmq.cloudnestbackend.exception.ErrorCode;
import com.cmq.cloudnestbackend.manager.CosManager;
import com.qcloud.cos.model.COSObject;
import com.qcloud.cos.model.COSObjectInputStream;
import com.qcloud.cos.utils.IOUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/file")
public class FileController {
    @Resource
    private CosManager cosManager;

    /**
     * 测试文件上传
     *
     * @param multipartFile 文件
     * @return 文件路径
     */
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    @PostMapping("/test/upload")
    public BaseResponse<String> testUploadFile(@RequestPart("file") MultipartFile multipartFile) {
        String fileName = multipartFile.getOriginalFilename();
        String filePath = String.format("/test/%s", fileName);
        File file = null;
        try {
            //创建临时文件
            file = File.createTempFile(filePath, null);
            multipartFile.transferTo(file);
            //上传文件到COS
            cosManager.putObject(filePath, file);
            return ResultUtils.success(filePath);
        } catch (Exception e) {
            log.error("file upload error: filepath = {}", filePath, e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "文件上传失败");
        } finally {
            //删除临时文件
            if (file != null && file.exists()) {
                boolean delete = file.delete();
                if (!delete) {
                    log.error("file delete error: filepath = {}", filePath);
                }
            }
        }
    }

    /**
     * 测试文件下载
     *
     * @param filepath 文件路径
     * @param response 响应对象
     */
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    @GetMapping("/test/download/")
    public void testDownloadFile(String filepath, HttpServletResponse response) throws IOException {
        COSObjectInputStream cosObjectInput = null;
        try {
            COSObject cosObject = cosManager.getObject(filepath);
            cosObjectInput = cosObject.getObjectContent();
            // 处理下载到的流
            byte[] bytes = IOUtils.toByteArray(cosObjectInput);
            // 设置响应头
            response.setContentType("application/octet-stream;charset=UTF-8");
            response.setHeader("Content-Disposition", "attachment; filename=" + filepath);
            // 写入响应
            response.getOutputStream().write(bytes);
            response.getOutputStream().flush();
        } catch (Exception e) {
            log.error("file download error, filepath = {}", filepath, e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "下载失败");
        } finally {
            // 关闭流
            if (cosObjectInput != null) {
                cosObjectInput.close();
            }
        }
    }

}
