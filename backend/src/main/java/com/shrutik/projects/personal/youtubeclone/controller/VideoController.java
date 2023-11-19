package com.shrutik.projects.personal.youtubeclone.controller;

import com.shrutik.projects.personal.youtubeclone.dto.CommentDTO;
import com.shrutik.projects.personal.youtubeclone.dto.UploadVideoResponse;
import com.shrutik.projects.personal.youtubeclone.dto.VideoDTO;
import com.shrutik.projects.personal.youtubeclone.service.UserService;
import com.shrutik.projects.personal.youtubeclone.service.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/videos")
public class VideoController {

    private final VideoService videoService;

    @PostMapping("/upload")
    @ResponseStatus(HttpStatus.CREATED)
    public UploadVideoResponse uploadVide(@RequestParam("file") MultipartFile file){
      return videoService.uploadVideo(file);
    }

    @PostMapping("/thumbnail")
    @ResponseStatus(HttpStatus.CREATED)
    public String uploadThumbnail( @RequestParam("file") MultipartFile file,@RequestParam("videoId") String videoId){
        return videoService.uploadThumbnail(file,videoId);
    }

    @PutMapping("/edit")
    @ResponseStatus(HttpStatus.OK)
    public VideoDTO editVideoMetadata(@RequestBody VideoDTO videoDTO){
      return  videoService.editVideo(videoDTO);
    }

    @GetMapping("/getVideoDetails/{videoId}")
    @ResponseStatus(HttpStatus.OK)
    public VideoDTO editVideoMetadata(@PathVariable String videoId){

        return videoService.getVideoDetails(videoId);
    }

    @PostMapping("/{videoId}/like")
    @ResponseStatus(HttpStatus.OK)
    public VideoDTO likeVideo( @PathVariable("videoId") String videoId){
        return videoService.likeVideo(videoId);
    }

    @PostMapping("/{videoId}/dislike")
    @ResponseStatus(HttpStatus.OK)
    public VideoDTO dislikeVideo( @PathVariable("videoId") String videoId){
        return videoService.dislikeVideo(videoId);
    }

    @PostMapping("/{videoId}/addComment")
    @ResponseStatus(HttpStatus.OK)
    public void addComment(@PathVariable("videoId") String videoId,@RequestBody CommentDTO commentDto){
         videoService.addComment(videoId,commentDto);
    }
    @GetMapping("/{videoId}/getComments")
    @ResponseStatus(HttpStatus.OK)
    public List<CommentDTO> getAllComments(@PathVariable("videoId") String videoId){
        return videoService.getAllCommentsByVideoId(videoId);
    }
    @GetMapping("/getAllVideos")
    @ResponseStatus(HttpStatus.OK)
    public List<VideoDTO> getAllVideos(){
        return videoService.getAllVideos();
    }


}
