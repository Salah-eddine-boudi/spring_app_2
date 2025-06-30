// src/main/java/com/example/audio_transcriber/dto/AudioView.java
package com.example.audio_transcriber.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AudioView {
    private String title;
    private String author;
    private String duration;
    /** nom du fichier audio avec extension, ex. "003353c1.mp3" */
    private String filename;
    /** transcription extraite de MongoDB (champ transcript_ar) */
    private String transcriptAr;
}
