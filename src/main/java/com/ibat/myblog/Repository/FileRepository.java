package com.ibat.myblog.Repository;

import com.ibat.myblog.Model.FileUpload;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends JpaRepository<FileUpload, Integer> {
}
