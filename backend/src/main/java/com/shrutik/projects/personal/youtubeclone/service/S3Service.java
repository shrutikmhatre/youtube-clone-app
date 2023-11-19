package com.shrutik.projects.personal.youtubeclone.service;


import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Service implements FileService {

    public static final String YOUTUBE_CLONE_VIDEO_BUCKET_NAME = "youtubeclone-video-bucket";
    private final AmazonS3Client amazonS3Client;
    @Override
    public String uploadFile(MultipartFile file){
        //upload file to AWS s3

        //prepare a key
        var filenameExtension = StringUtils.getFilenameExtension(file.getOriginalFilename());

        var key = UUID.randomUUID().toString()+"."+filenameExtension;

        var metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());

        try {
            amazonS3Client.putObject(YOUTUBE_CLONE_VIDEO_BUCKET_NAME,key,file.getInputStream(),metadata);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "An exception occurred while uploading the file");
        }
        amazonS3Client.setObjectAcl(YOUTUBE_CLONE_VIDEO_BUCKET_NAME, key,CannedAccessControlList.PublicRead);

        return   amazonS3Client.getResourceUrl(YOUTUBE_CLONE_VIDEO_BUCKET_NAME,key);
    }
}
