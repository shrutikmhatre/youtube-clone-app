package com.shrutik.projects.personal.youtubeclone.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shrutik.projects.personal.youtubeclone.dto.UserInfoDTO;
import com.shrutik.projects.personal.youtubeclone.model.User;
import com.shrutik.projects.personal.youtubeclone.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserRegistrationService {

    @Value("${auth0.userInfoEndpoint}")
    private String userInfoEndpoint;

    private final UserRepository userRepository;
    public String registerUser(String tokenValue){
        //Make a call to uer info endpoint
       HttpRequest httpRequest =  HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(userInfoEndpoint))
                .setHeader("Authorization",String.format("Bearer %s",tokenValue))
                .build();

        HttpClient httpClient =HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .build();
        try {
          HttpResponse<String> responseString =   httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
          String body = responseString.body();

          ObjectMapper objectMapper = new ObjectMapper();
          objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
          UserInfoDTO userInfoDTO =objectMapper.readValue(body, UserInfoDTO.class);
            Optional<User> userBySubject  =  userRepository.findBySub(userInfoDTO.getSub());
        // =  new User();
            if(userBySubject.isPresent()){
                return userBySubject.get().getId();
            }else {
                User user = new User();
                user.setFirstName(userInfoDTO.getGivenName());
                user.setLastName(userInfoDTO.getFamilyName());
                user.setFullName(userInfoDTO.getName());
                user.setEmailAddress(userInfoDTO.getEmail());
                user.setSub(userInfoDTO.getSub());
                return userRepository.save(user).getId();

            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Exception occurred while registering user",e);
        }
        //fetch user details and save them to the database

    }
}
