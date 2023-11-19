package com.shrutik.projects.personal.youtubeclone.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface FileService {
    public String uploadFile(MultipartFile file);
}
