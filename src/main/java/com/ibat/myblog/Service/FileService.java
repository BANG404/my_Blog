package com.ibat.myblog.Service;

import com.ibat.myblog.Model.FileUpload;
import com.ibat.myblog.Repository.FileRepository;
import com.ibat.myblog.Util.FileUtil;
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

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private FileUtil fileUtil;

    public FileUpload storeFile(MultipartFile file) throws IOException {
        String fileName = fileUtil.generateUniqueFileName(file.getOriginalFilename());
        String filePath = fileUtil.saveFile(file, fileName);
        
        FileUpload fileUpload = new FileUpload();
        fileUpload.setFilename(fileName);
        fileUpload.setContentType(file.getContentType());
        fileUpload.setSize(file.getSize());
        fileUpload.setUrl(filePath);
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
