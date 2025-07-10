package com.example.audio_transcriber.repository.mongo;

import com.example.audio_transcriber.model.AudioTranscript;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface AudioTranscriptRepository
        extends MongoRepository<AudioTranscript, String> {

    /** Cherche tous les documents dont transcriptFr contient le mot clé */
    List<AudioTranscript> findByTranscriptFrContainingIgnoreCase(String keyword);

    /** Idem en arabe */
    List<AudioTranscript> findByTranscriptArContainingIgnoreCase(String keyword);

    /** Recherche par nom de fichier (sans extension) */
    AudioTranscript findBySoundfileName(String soundfileName);

    /** Recherche multiple sur une liste de soundfileName */
    List<AudioTranscript> findBySoundfileNameIn(Collection<String> names);
}
