package com.netflix.clone.netflix_clone_backend.util;

import com.netflix.clone.netflix_clone_backend.dao.UserRepository;
import com.netflix.clone.netflix_clone_backend.dao.VideoRepository;
import com.netflix.clone.netflix_clone_backend.entity.User;
import com.netflix.clone.netflix_clone_backend.entity.Video;
import com.netflix.clone.netflix_clone_backend.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ServiceUtil {
    private final UserRepository userRepository;
    private final VideoRepository videoRepository;

    public User getUserByEmailOrThrow(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: "+email));
    }

    public User getUserByIdOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with Id: "+id));
    }

    public Video getVideoByIdOrThrow(Long id) {
        return videoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Video not found with Id: "+id));
    }
}
