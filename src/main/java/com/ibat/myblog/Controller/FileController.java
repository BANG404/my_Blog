package com.ibat.myblog.Controller;

import com.ibat.myblog.Model.FileUpload;
import com.ibat.myblog.Service.FileService;
import com.ibat.myblog.Util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/files")
public class FileController {

    @Autowired
    private FileService fileService;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {
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

    @DeleteMapping("/{fileId}")
    public ResponseEntity<?> deleteFile(@PathVariable Integer fileId) {
        try {
            fileService.deleteFile(fileId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to delete file: " + e.getMessage());
        }
    }
}
