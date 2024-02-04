package com.lilkhalil.music.dao;

import com.lilkhalil.music.domain.Audio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AudioRepository extends JpaRepository<Audio, Long> {
    Optional<Audio> findByName(String name);
}
