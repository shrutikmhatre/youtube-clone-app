package com.shrutik.projects.personal.youtubeclone.service;

import com.shrutik.projects.personal.youtubeclone.model.User;
import com.shrutik.projects.personal.youtubeclone.model.Video;
import com.shrutik.projects.personal.youtubeclone.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User getCurrentUser(){
      String sub =   ((Jwt)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getClaim("sub");
        return userRepository.findBySub(sub)
                .orElseThrow(()->new IllegalArgumentException("Cannot find user with the sub->"+sub));
    }

    public void addToLikedVideos(String videoId) {
        User currentUser = getCurrentUser();
        currentUser.addToLikedVideos(videoId);
        userRepository.save(currentUser);
    }

    public boolean ifLikedVideo(String videoId){
       return getCurrentUser().getLikedVideos().stream().anyMatch(likedVideo->likedVideo.equals(videoId));
    }

    public void removeFromLikedVideo(String videoId) {
        User currentUser = getCurrentUser();
        currentUser.removeFromLikedVideos(videoId);
        userRepository.save(currentUser);
    }

    public boolean ifDislikedVideo(String videoId) {
        return getCurrentUser().getDislikedVideos().stream().anyMatch(dislikedVideo->dislikedVideo.equals(videoId));
    }

    public void removeFromDislikedVideos(String videoId) {
        User currentUser = getCurrentUser();
        currentUser.removeFromDislikedVideos(videoId);
        userRepository.save(currentUser);
    }

    public void addToDisLikedVideo(String videoId) {
        User currentUser = getCurrentUser();
        currentUser.addToDisLikeVideo(videoId);
        userRepository.save(currentUser);
    }

    public void addVideoToHistory(String videoId) {
        User currentUser = getCurrentUser();
        currentUser.addToVideoHistory(videoId);
        userRepository.save(currentUser);
    }

    public void subscribeUser(String userId) {
        //retrieve the current user and add the userId to subscribe to users set.

        //Retrieve the target user and the current user to the subscribers list.
        User currentUser = getCurrentUser();
        currentUser.addToSubscribedToUsers(userId);

       User user = getUserById(userId);

        user.addToSubscribers(currentUser.getId());

        userRepository.save(currentUser);
        userRepository.save(user);

    }

    public void unSubscribeUser(String userId) {
        //retrieve the current user and add the userId to subscribe to users set.

        //Retrieve the target user and the current user to the subscribers list.
        User currentUser = getCurrentUser();
        currentUser.removeFromSubscribedToUsers(userId);

        User user =  getUserById(userId);

        user.removeFromSubscribers(currentUser.getId());

        userRepository.save(currentUser);
        userRepository.save(user);

    }

    User getUserById(String userId){
        return  userRepository.findById(userId).
                orElseThrow(() -> new IllegalArgumentException("cannot find user with user id"+userId));
    }
    public Set<String> getUserVideoHistory(String userId) {
       User user = getUserById(userId);
       return user.getVideoHistory();

    }
}
