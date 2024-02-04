package com.lilkhalil.music.controller;

import com.lilkhalil.music.domain.Audio;
import com.lilkhalil.music.service.AudioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/music")
public class AudioController {

    @Autowired
    private AudioService audioService;

    @PostMapping
    public Audio addAudio(@RequestBody Audio audio) {
        return audioService.addAudio(audio);
    }

    @GetMapping
    public List<Audio> getAudios() {
        return audioService.getAudios();
    }

    @GetMapping("/{id}")
    public Audio getAudioById(@PathVariable("id") Long id) {
        return audioService.getAudioById(id);
    }

    @PostMapping("/find")
    public Audio getAudioByName(@RequestParam("name") String name) {
        return audioService.getAudioByName(name);
    }

    @PostMapping("/{id}/image")
    public Audio uploadImage(@PathVariable("id") Long id, @RequestParam("file") MultipartFile file) {
        return audioService.uploadImage(id, file);
    }

    @PostMapping("/{id}/audio")
    public Audio uploadAudio(@PathVariable("id") Long id, @RequestParam("file") MultipartFile file) {
        return audioService.uploadAudio(id, file);
    }

    @PutMapping("/{id}")
    public Audio editAudio(@PathVariable("id") Long id, @RequestBody Audio audio) {
        return audioService.editAudio(id, audio);
    }

    @DeleteMapping("/{id}")
    public void deleteAudioById(@PathVariable("id") Long id) {
        audioService.deleteAudioById(id);
    }

}
