package com.shrutik.projects.personal.youtubeclone.service;

import com.shrutik.projects.personal.youtubeclone.dto.CommentDTO;
import com.shrutik.projects.personal.youtubeclone.dto.UploadVideoResponse;
import com.shrutik.projects.personal.youtubeclone.dto.VideoDTO;
import com.shrutik.projects.personal.youtubeclone.model.Comment;
import com.shrutik.projects.personal.youtubeclone.model.Video;
import com.shrutik.projects.personal.youtubeclone.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VideoService {
    private final S3Service s3Service;
    private final VideoRepository videoRepository;
    private final UserService userService;
    public UploadVideoResponse uploadVideo(MultipartFile file){
        //upload video to AWS S3
        //Save video data to database
        String videoUrl =   s3Service.uploadFile(file);
        var video = new Video();
        video.setVideoUrl(videoUrl);
        var savedVideo=  videoRepository.save(video);
        UploadVideoResponse response =  new UploadVideoResponse();
        return new UploadVideoResponse(savedVideo.getId(),savedVideo.getVideoUrl());
    }

    public VideoDTO editVideo(VideoDTO videoDTO) {
        //find video by video id
          var savedVideo = getVideoById(videoDTO.getId());
        //map the video dto fields  to video
            savedVideo.setTitle(videoDTO.getTitle());
            savedVideo.setDescription(videoDTO.getDescription());
            savedVideo.setTags(videoDTO.getTags());
            savedVideo.setVideoStatus(videoDTO.getVideoStatus());
            savedVideo.setThumbnailUrl(videoDTO.getThumbnailUrl());
        //save the video to the database
            videoRepository.save(savedVideo);
            return videoDTO;
    }

    public String uploadThumbnail(MultipartFile file, String videoId) {
      var  savedVideo = getVideoById(videoId);

      String thumbnailUrl =    s3Service.uploadFile(file);

      savedVideo.setThumbnailUrl(thumbnailUrl);
      videoRepository.save(savedVideo);
      return thumbnailUrl;
    }

    Video getVideoById(String videoId){
        return  videoRepository.findById(videoId)
                .orElseThrow(() -> new IllegalArgumentException("Cannot find video by id -"+videoId));

    }

    public VideoDTO getVideoDetails(String videoId) {
      Video savedVideo =  getVideoById(videoId);

      increaseViewCount(savedVideo);
      userService.addVideoToHistory(videoId);

      return mapToVideoDto(savedVideo);
    }

    private VideoDTO mapToVideoDto(Video savedVideo) {
        VideoDTO videoDto = new VideoDTO();
        videoDto.setVideoUrl(savedVideo.getVideoUrl());
        videoDto.setThumbnailUrl(savedVideo.getThumbnailUrl());
        videoDto.setId(savedVideo.getId());
        videoDto.setTitle(savedVideo.getTitle());
        videoDto.setDescription(savedVideo.getDescription());
        videoDto.setTags(savedVideo.getTags());
        videoDto.setVideoStatus(savedVideo.getVideoStatus());
        videoDto.setLikeCount(savedVideo.getLikes().get());
        videoDto.setDislikeCount(savedVideo.getDislikes().get());
        videoDto.setViewCount(savedVideo.getViewCount().get());
        return  videoDto;
    }

    private void increaseViewCount(Video savedVideo) {
        savedVideo.incrementViewCount();
        videoRepository.save(savedVideo);
    }

    public VideoDTO likeVideo(String videoId) {
        //get video by id
        Video video =getVideoById(videoId);

        /*Increment the like count
        If user already liked the video decrement the like count
        If user already disliked the video , then increment like count and decrement dislike count*/

        if(userService.ifLikedVideo(videoId)){//if clicked on like again
            if(video.getLikes().get()>0) video.decrementLike();
            userService.removeFromLikedVideo(videoId);
        }else if(userService.ifDislikedVideo(videoId)){ // if clicked on dislike and then like
            if(video.getDislikes().get()>0) video.decrementDislike();
            userService.removeFromDislikedVideos(videoId);
            video.incrementLike();
            userService.addToLikedVideos(videoId);
        }else{ //normal like
            video.incrementLike();
            userService.addToLikedVideos(videoId);
        }
         videoRepository.save(video);

        return mapToVideoDto(video);
    }

    public VideoDTO dislikeVideo(String videoId) {
        {
            //get video by id
            Video video =getVideoById(videoId);

        /*Increment the like count
        If user already liked the video decrement the like count
        If user already disliked the video , then increment like count and decrement dislike count*/

            if(userService.ifDislikedVideo(videoId)){//if clicked on dislike again
                if(video.getDislikes().get()>0) video.decrementDislike();
                userService.removeFromDislikedVideos(videoId);
            }else if(userService.ifLikedVideo(videoId)){ // if clicked on like and then dislike
                if(video.getLikes().get()>0) video.decrementLike();
                userService.removeFromLikedVideo(videoId);
                video.incrementDislike();
                userService.addToDisLikedVideo(videoId);
            }else{ //normal dislike
                video.incrementDislike();
                userService.addToDisLikedVideo(videoId);
            }
            videoRepository.save(video);
            return mapToVideoDto(video);

        }
    }

    public void addComment(String videoId, CommentDTO commentDto) {
        Video  video = getVideoById(videoId);
        Comment comment = new Comment();
        comment.setText(commentDto.getCommentText());
        comment.setAuthorId(commentDto.getAuthorId());
        video.addComment(comment);
        videoRepository.save(video);
    }

    public List<CommentDTO> getAllCommentsByVideoId(String videoId) {
        Video video = getVideoById(videoId);
        List<Comment> commentList = video.getCommentList();

        return commentList.stream().map(this::mapToCommentDto).toList();

    }

    private CommentDTO mapToCommentDto(Comment comment) {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setCommentText(comment.getText());
        commentDTO.setAuthorId(comment.getAuthorId());
        return commentDTO;
    }

    public List<VideoDTO> getAllVideos() {
        return videoRepository.findAll().stream().map(this::mapToVideoDto).toList();
    }
}
