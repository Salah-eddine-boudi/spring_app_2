// AudioTranscriptRepository.java
package com.example.audio_transcriber.repository.mongo;

import com.example.audio_transcriber.model.AudioTranscript;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Collection;
import java.util.List;

public interface AudioTranscriptRepository
        extends MongoRepository<AudioTranscript, String> {
    List<AudioTranscript> findBySoundfileNameIn(Collection<String> names);
}
