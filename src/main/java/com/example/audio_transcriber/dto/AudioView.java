package com.example.audio_transcriber.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AudioView {
    private String title;
    private String author;
    private String duration;
    private String filename;
    private String transcript;  // fusion fr/ar

    private String interpret;
    private String keywords;
    private String compagnie;
    private String album;
    private String recordDate;
    private String lastModifTime;
    private String commentaire1;
    private String commentaire2;
    private String commentaire3;
}
