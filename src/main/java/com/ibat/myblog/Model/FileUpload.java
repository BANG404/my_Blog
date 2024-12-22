package com.ibat.myblog.Model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "file_uploads")
public class FileUpload {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    private Integer userId;
    
    @Column(length = 255)
    private String filename;
    
    @Column(length = 100)
    private String contentType;
    
    @Column(length = 1000)
    private String url;
    
    private Long size;
    
    private LocalDateTime uploadTime;
}
