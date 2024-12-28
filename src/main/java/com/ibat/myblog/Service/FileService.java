package com.ibat.myblog.Service;

import com.ibat.myblog.Model.FileUpload;
import com.ibat.myblog.Repository.FileRepository;
import com.ibat.myblog.Utils.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;

@Service
public class FileService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Value("${app.base-url}")
    private String baseUrl;

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private FileUtil fileUtil;

    public FileUpload storeFile(MultipartFile file, Integer userId) throws IOException {
        String fileName = fileUtil.generateUniqueFileName(file.getOriginalFilename());
        String filePath = fileUtil.saveFile(file, fileName);
        
        FileUpload fileUpload = new FileUpload();
        fileUpload.setUserId(userId);
        fileUpload.setFilename(fileName);
        fileUpload.setContentType(file.getContentType());
        fileUpload.setSize(file.getSize());
        // 生成可访问的URL
        fileUpload.setUrl(baseUrl + "/uploads/" + fileName);
        fileUpload.setUploadTime(LocalDateTime.now());
        
        return fileRepository.save(fileUpload);
    }

    public void deleteFile(Integer fileId) throws IOException {
        FileUpload file = fileRepository.findById(fileId)
                .orElseThrow(() -> new RuntimeException("File not found"));
        
        fileUtil.deleteFile(file.getUrl());
        fileRepository.delete(file);
    }
}
