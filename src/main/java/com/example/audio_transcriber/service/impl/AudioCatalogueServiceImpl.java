package com.example.audio_transcriber.service.impl;

import com.example.audio_transcriber.dto.AudioView;
import com.example.audio_transcriber.model.AudioFileInfo;
import com.example.audio_transcriber.model.AudioTranscript;
import com.example.audio_transcriber.repository.jpa.AudioFileInfoRepository;
import com.example.audio_transcriber.repository.mongo.AudioTranscriptRepository;
import com.example.audio_transcriber.service.AudioCatalogueService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AudioCatalogueServiceImpl implements AudioCatalogueService {

    private final AudioFileInfoRepository mysqlRepo;
    private final AudioTranscriptRepository mongoRepo;

    /** Cache optionnel : nécessite spring-boot-starter-cache + @EnableCaching */
    @Override
    @Cacheable("catalogue")
    public List<AudioView> getCatalogue() {

        // 1) lire les métadonnées MySQL
        List<AudioFileInfo> metas = mysqlRepo.findAll();
        if (metas.isEmpty()) return List.of();

        // 2) récupérer TOUTES les transcriptions correspondantes en une seule requête
        List<String> keys = metas.stream()
                                 .map(AudioFileInfo::getSoundfileName)
                                 .collect(Collectors.toList());

        // Add this method to your AudioTranscriptRepository interface:
        // List<AudioTranscript> findBySoundfileNameIn(List<String> soundfileNames);

        Map<String, String> transcriptMap = mongoRepo.findBySoundfileNameIn(keys)
            .stream()
            .collect(Collectors.toMap(
                AudioTranscript::getSoundfileName,
                AudioTranscript::getTranscript_fr
            ));

        // 3) fusionner proprement
        return metas.stream()
                .map(meta -> merge(meta, transcriptMap.get(meta.getSoundfileName())))
                .toList();
    }

    /* ---------- helpers ---------- */

    private AudioView merge(AudioFileInfo meta, String transcript) {

        return new AudioView(
                meta.getTitle(),
                Optional.ofNullable(meta.getAuthor()).orElse("—"),
                formatDuration(meta.getDureeMs(), meta.getDuree()),
                meta.getSoundfileName(),
                transcript != null ? transcript : "—"
        );
    }

    /** Convertit "123456" ms ou "123" secondes vers "MM:SS" lisible */
    private String formatDuration(String dureeMs, String duree) {
        try {
            long millis = dureeMs != null ? Long.parseLong(dureeMs) :
                         duree != null   ? Long.parseLong(duree) * 1000 : 0;

            if (millis == 0) return "—";

            Duration d = Duration.ofMillis(millis);
            long minutes = d.toMinutes();
            long seconds = d.toSecondsPart();
            return String.format("%02d:%02d", minutes, seconds);
        } catch (NumberFormatException e) {
            return duree != null ? duree : "—";
        }
    }
}
