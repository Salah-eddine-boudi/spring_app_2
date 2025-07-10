package com.example.audio_transcriber.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "audioFiles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AudioTranscript {

    @Id
    private String id;

    /** On stocke le nom du fichier sans extension */
    private String soundfileName;

    /** Transcription en français (MongoDB stocke « transcript_fr ») */
    @Field("transcript_fr")
    private String transcriptFr;

    /** Transcription en arabe (MongoDB stocke « transcript_ar ») */
    @Field("transcript_ar")
    private String transcriptAr;
}
