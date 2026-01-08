package com.netflix.clone.netflix_clone_backend.dto.response;

import com.netflix.clone.netflix_clone_backend.entity.Video;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VideoResponse {
    private Long id;
    private String title;
    private String description;
    private Integer year;
    private String rating;
    private Integer duration;
    private String src;
    private String poster;
    private Boolean published;
    private List<String> categories;
    private Instant createdAt;
    private Instant updatedAt;
    private Boolean isWatchlist;

    public static VideoResponse fromEntity(Video video) {
        return new VideoResponse(
                video.getId(),
                video.getTitle(),
                video.getDescription(),
                video.getYear(),
                video.getRating(),
                video.getDuration(),
                video.getSrc(),
                video.getPoster(),
                video.isPublished(),
                video.getCategories(),
                video.getCreatedAt(),
                video.getUpdatedAt(),
                video.isInWatchList()
        );
    }
}
