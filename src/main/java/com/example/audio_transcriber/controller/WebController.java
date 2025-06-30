package com.example.audio_transcriber.controller;

import com.example.audio_transcriber.service.AudioCatalogueService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class WebController {

    private final AudioCatalogueService catalogueService;

    @Value("${app.audio.folder}")
    private String audioFolder;

    /** 1) Méthode pour servir la page Thymeleaf 'index.html' 
     *   et y injecter la liste des audios sous 'audios' **/
    @GetMapping("/")
    public String home(Model model) {
        List<?> audios = catalogueService.getCatalogue();
        log.debug("Chargement du catalogue, {} entrées", audios.size());
        model.addAttribute("audios", audios);
        // Thymeleaf va traiter templates/index.html
        return "index";
    }

    /** 2) Méthode pour streamer les fichiers audio du disque **/
    @GetMapping("/audio/stream/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> streamAudio(@PathVariable String filename) {
        try {
            Path file = Paths.get(audioFolder).resolve(filename);
            log.debug("Demande de streaming pour '{}'", file);

            if (!Files.exists(file) || !Files.isReadable(file)) {
                // fallback .mp3 si nécessaire
                String alt = filename.replaceAll("\\.(?i)wav$", ".mp3");
                Path mp3 = Paths.get(audioFolder).resolve(alt);
                if (Files.exists(mp3) && Files.isReadable(mp3)) {
                    log.debug("Basculé sur fallback '{}'", mp3);
                    file = mp3;
                } else {
                    log.warn("Audio introuvable: ni {} ni {}", filename, alt);
                    return ResponseEntity.notFound().build();
                }
            }

            Resource resource = new UrlResource(file.toUri());
            String contentType = Files.probeContentType(file);
            long length = Files.size(file);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE,
                            contentType != null ? contentType : "application/octet-stream")
                    .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(length))
                    .body(resource);

        } catch (Exception e) {
            log.error("Erreur streaming audio", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
