package com.ibat.myblog.Controller;

import com.ibat.myblog.Model.FileUpload;
import com.ibat.myblog.Service.FileService;
import com.ibat.myblog.Util.FileUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Tag(name = "文件管理", description = "文件上传下载接口")
@RestController
@RequestMapping("/api/files")
public class FileController {
    
    @Autowired
    private FileService fileService;

    @Operation(summary = "上传文件", description = "上传单个文件")
    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(
            @Parameter(description = "要上传的文件", required = true)
            @RequestParam("file") MultipartFile file) {
        try {
            FileUpload uploadedFile = fileService.storeFile(file);
            Map<String, Object> response = new HashMap<>();
            response.put("filename", uploadedFile.getFilename());
            response.put("url", uploadedFile.getUrl());
            response.put("size", uploadedFile.getSize());
            response.put("contentType", uploadedFile.getContentType());
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Failed to upload file: " + e.getMessage());
        }
    }

    @Operation(summary = "删除文件", description = "通过文件ID删除已上传的文件")
    @DeleteMapping("/{fileId}")
    public ResponseEntity<?> deleteFile(
            @Parameter(description = "要删除的文件ID", required = true)
            @PathVariable Integer fileId) {
        try {
            fileService.deleteFile(fileId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to delete file: " + e.getMessage());
        }
    }
}
