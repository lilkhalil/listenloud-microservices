package com.lilkhalil.music.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "music")
public class Audio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    private String audioId;
    private String description;
    private String imageId;

    @Column(nullable = false)
    private Long userId;

    private List<Tag> tags;

    enum Tag {
        RAP, ROCK, JAZZ, POP, DOTA
    }

}
