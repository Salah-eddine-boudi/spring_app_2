package com.example.audio_transcriber.controller;

import com.example.audio_transcriber.model.AudioTranscript;
import com.example.audio_transcriber.repository.mongo.AudioTranscriptRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/audio")
@RequiredArgsConstructor
public class AudioSearchController {

    private final AudioTranscriptRepository repo;

    /** GET /api/audio/search?keyword=bonjour */
    @GetMapping("/search")
    public List<AudioTranscript> searchFr(@RequestParam String keyword) {
        return repo.findByTranscriptFrContainingIgnoreCase(keyword);
    }

    /** GET /api/audio/search/ar?keyword=مرحبا */
    @GetMapping("/search/ar")
    public List<AudioTranscript> searchAr(@RequestParam String keyword) {
        return repo.findByTranscriptArContainingIgnoreCase(keyword);
    }
}
