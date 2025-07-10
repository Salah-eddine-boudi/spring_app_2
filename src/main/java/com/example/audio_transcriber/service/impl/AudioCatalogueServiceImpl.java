package com.example.audio_transcriber.service.impl;

import com.example.audio_transcriber.dto.AudioView;
import com.example.audio_transcriber.model.AudioFileInfo;
import com.example.audio_transcriber.model.AudioTranscript;
import com.example.audio_transcriber.repository.jpa.AudioFileInfoRepository;
import com.example.audio_transcriber.repository.mongo.AudioTranscriptRepository;
import com.example.audio_transcriber.service.AudioCatalogueService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.*;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AudioCatalogueServiceImpl implements AudioCatalogueService {

    private final AudioFileInfoRepository mysqlRepo;
    private final AudioTranscriptRepository mongoRepo;

    @Value("${app.audio.folder}")
    private String audioFolder;

    @Override
    @Cacheable("catalogue")
    public List<AudioView> getCatalogue() {
        try {
            // 1. Lire les fichiers audio
            List<Path> files = Files.walk(Paths.get(audioFolder), 1)
                    .filter(Files::isRegularFile)
                    .filter(p -> p.toString().toLowerCase().endsWith(".mp3") || p.toString().toLowerCase().endsWith(".wav"))
                    .collect(Collectors.toList());

            List<String> filenames = files.stream()
                    .map(p -> p.getFileName().toString())
                    .toList();

            // 2. Clés sans extension
            Set<String> keysSansExtension = filenames.stream()
                    .map(this::stripExtension)
                    .collect(Collectors.toSet());

            // 3. Métadonnées MySQL
            List<AudioFileInfo> metas = mysqlRepo.findAll();
            Map<String, AudioFileInfo> metaMap = metas.stream()
                    .collect(Collectors.toMap(
                            a -> a.getSoundfileName().toLowerCase(),
                            a -> a,
                            (a1, a2) -> a1
                    ));

            // 4. Transcriptions MongoDB
            Map<String, AudioTranscript> transcriptMap = mongoRepo.findBySoundfileNameIn(keysSansExtension)
                    .stream()
                    .collect(Collectors.toMap(
                            a -> a.getSoundfileName().toLowerCase(),
                            a -> a,
                            (a1, a2) -> a1
                    ));

            // 5. Fusion
            List<AudioView> result = new ArrayList<>();
            for (String filename : filenames) {
                String keyLower = filename.toLowerCase();
                String keySansExt = stripExtension(keyLower);

                AudioFileInfo info = metaMap.get(keyLower);
                if (info == null) {
                    String alt = keyLower.endsWith(".mp3") ? keyLower.replace(".mp3", ".wav") : keyLower.replace(".wav", ".mp3");
                    info = metaMap.get(alt);
                }

                // ✅ Chercher transcript avec nom sans extension
                AudioTranscript transcriptObj = transcriptMap.get(keySansExt);
                String transcript = null;
                if (transcriptObj != null) {
                    transcript = Optional.ofNullable(transcriptObj.getTranscriptFr())
                            .filter(s -> !s.isBlank())
                            .orElse(transcriptObj.getTranscriptAr());
                }

                // ✅ Construction AudioView
                result.add(new AudioView(
                        info != null && notEmpty(info.getTitle()) ? info.getTitle() : filename,
                        info != null && notEmpty(info.getAuthor()) ? info.getAuthor() : "—",
                        info != null ? formatDuration(info.getDureeMs(), info.getDuree()) : "??:??",
                        filename,
                        notEmpty(transcript) ? transcript : "Pas de transcription disponible",
                        info != null && notEmpty(info.getInterpret()) ? info.getInterpret() : "En cours de traitement",
                        info != null && notEmpty(info.getKeywords()) ? info.getKeywords() : "En cours de traitement",
                        info != null && notEmpty(info.getCompagnyDispName()) ? info.getCompagnyDispName() : "En cours de traitement",
                        info != null && notEmpty(info.getAlbumDispName()) ? info.getAlbumDispName() : "En cours de traitement",
                        info != null && notEmpty(info.getRecordDate()) ? info.getRecordDate() : "—",
                        info != null && notEmpty(info.getLastModifTime()) ? info.getLastModifTime() : "—",
                        info != null && notEmpty(info.getCommentaire1()) ? info.getCommentaire1() : "—",
                        info != null && notEmpty(info.getCommentaire2()) ? info.getCommentaire2() : "—",
                        info != null && notEmpty(info.getCommentaire3()) ? info.getCommentaire3() : "—"
                ));
            }

            return result;

        } catch (IOException e) {
            log.error("Erreur lors du chargement du catalogue audio", e);
            return Collections.emptyList();
        }
    }

    private String stripExtension(String filename) {
        int dot = filename.lastIndexOf(".");
        return dot != -1 ? filename.substring(0, dot).toLowerCase() : filename.toLowerCase();
    }

    private String formatDuration(String dureeMs, String duree) {
        try {
            long millis = dureeMs != null ? Long.parseLong(dureeMs) :
                          duree != null ? Long.parseLong(duree) * 1000 : 0;
            if (millis == 0) return "—";
            Duration d = Duration.ofMillis(millis);
            return String.format("%02d:%02d", d.toMinutes(), d.toSecondsPart());
        } catch (NumberFormatException e) {
            return duree != null ? duree : "—";
        }
    }

    private boolean notEmpty(String s) {
        return s != null && !s.isBlank();
    }
}
