package com.example.audio_transcriber.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Document stocké dans la collection "audioFiles" de MongoDB.
 * La clé de jointure est soundfileName (ou id si absent).
 */
@Document(collection = "audioFiles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AudioTranscript {

    @Id                 // _id MongoDB
    private String id;  // ex. 0A9B2D53 ou même nom que le fichier

    /** Redondant mais pratique pour la jointure avec MySQL */
    private String soundfileName;

    /** Transcription en français */
    private String transcript_fr;

    /* Autres champs possibles :
       private String asr_model;
       private String language;
       etc.  -> facultatifs si non utilisés dans ton appli */
}
