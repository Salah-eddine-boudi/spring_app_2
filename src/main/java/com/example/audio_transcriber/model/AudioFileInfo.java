package com.example.audio_transcriber.model;

import jakarta.persistence.*;

@Entity
@Table(name = "test_1")
public class AudioFileInfo {

    @Id
    @Column(name = "title_id")
    private String titleId;

    @Column(name = "title")
    private String title;

    @Column(name = "soundfile_name")
    private String soundfileName;

    @Column(name = "author")
    private String author;

    @Column(name = "duree_ms")
    private String dureeMs;

    @Column(name = "duree")
    private String duree;

    @Column(name = "is_online")
    private String isOnline;

    @Column(name = "record_date")
    private String recordDate;

    @Column(name = "last_modif_time")
    private String lastModifTime;

    @Column(name = "interpret")
    private String interpret;

    @Column(name = "keywords")
    private String keywords;

    @Column(name = "compagny_disp_name")
    private String compagnyDispName;

    @Column(name = "album_disp_name")
    private String albumDispName;

    @Column(name = "commentaire1")
    private String commentaire1;

    @Column(name = "commentaire2")
    private String commentaire2;

    @Column(name = "commentaire3")
    private String commentaire3;

    @Column(name = "class_code")
    private String classCode;

    @Column(name = "class_name")
    private String className;

    @Column(name = "class_id")
    private String classId;

    public AudioFileInfo() {}

    // -- Getters & Setters --

    public String getTitleId() { return titleId; }
    public void setTitleId(String titleId) { this.titleId = titleId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getSoundfileName() { return soundfileName; }
    public void setSoundfileName(String soundfileName) { this.soundfileName = soundfileName; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getDureeMs() { return dureeMs; }
    public void setDureeMs(String dureeMs) { this.dureeMs = dureeMs; }

    public String getDuree() { return duree; }
    public void setDuree(String duree) { this.duree = duree; }

    public String getIsOnline() { return isOnline; }
    public void setIsOnline(String isOnline) { this.isOnline = isOnline; }

    public String getRecordDate() { return recordDate; }
    public void setRecordDate(String recordDate) { this.recordDate = recordDate; }

    public String getLastModifTime() { return lastModifTime; }
    public void setLastModifTime(String lastModifTime) { this.lastModifTime = lastModifTime; }

    public String getInterpret() { return interpret; }
    public void setInterpret(String interpret) { this.interpret = interpret; }

    public String getKeywords() { return keywords; }
    public void setKeywords(String keywords) { this.keywords = keywords; }

    public String getCompagnyDispName() { return compagnyDispName; }
    public void setCompagnyDispName(String compagnyDispName) { this.compagnyDispName = compagnyDispName; }

    public String getAlbumDispName() { return albumDispName; }
    public void setAlbumDispName(String albumDispName) { this.albumDispName = albumDispName; }

    public String getCommentaire1() { return commentaire1; }
    public void setCommentaire1(String commentaire1) { this.commentaire1 = commentaire1; }

    public String getCommentaire2() { return commentaire2; }
    public void setCommentaire2(String commentaire2) { this.commentaire2 = commentaire2; }

    public String getCommentaire3() { return commentaire3; }
    public void setCommentaire3(String commentaire3) { this.commentaire3 = commentaire3; }

    public String getClassCode() { return classCode; }
    public void setClassCode(String classCode) { this.classCode = classCode; }

    public String getClassName() { return className; }
    public void setClassName(String className) { this.className = className; }

    public String getClassId() { return classId; }
    public void setClassId(String classId) { this.classId = classId; }
}
