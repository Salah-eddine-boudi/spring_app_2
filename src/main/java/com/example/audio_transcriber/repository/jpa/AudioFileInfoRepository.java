// AudioFileInfoRepository.java
package com.example.audio_transcriber.repository.jpa;

import com.example.audio_transcriber.model.AudioFileInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AudioFileInfoRepository
        extends JpaRepository<AudioFileInfo, String> {}
