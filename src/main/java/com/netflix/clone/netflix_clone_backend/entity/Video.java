package com.netflix.clone.netflix_clone_backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "video")
@Getter
@Setter
public class Video {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(length = 4000)
    private String description;

    private Integer year;
    private String rating;
    private Integer duration;

    @Column(name = "src")
    @JsonIgnore
    private String srcUUID;

    @Column(name = "poster")
    @JsonIgnore
    private String posterUUID;

    @Column(nullable = false)
    private boolean published = false;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "video_categories", joinColumns = @JoinColumn(name = "video_id"))
    @Column(name = "category")
    private List<String> categories = new ArrayList<>();

    @CreationTimestamp
    @Column(nullable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private Instant updatedAt;

    @Transient
    @JsonProperty("isInWatchlist")
    private boolean isInWatchList;

    @JsonProperty("src")
    public String getSrc() {
        if(srcUUID != null && !srcUUID.isEmpty()) {
            String baseURI = ServletUriComponentsBuilder.fromCurrentContextPath().toUriString();
            return baseURI + "/api/files/video/" + srcUUID;
        }
        return null;
    }

    @JsonProperty("poster")
    public String getPoster() {
        if(posterUUID != null && !posterUUID.isEmpty()) {
            String baseURI = ServletUriComponentsBuilder.fromCurrentContextPath().toUriString();
            return baseURI + "/api/files/video/" + posterUUID;
        }
        return null;
    }
}
