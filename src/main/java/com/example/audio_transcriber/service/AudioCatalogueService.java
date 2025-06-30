package com.example.audio_transcriber.service;
import com.example.audio_transcriber.dto.AudioView;
import java.util.List;

public interface AudioCatalogueService {
    List<AudioView> getCatalogue();
}