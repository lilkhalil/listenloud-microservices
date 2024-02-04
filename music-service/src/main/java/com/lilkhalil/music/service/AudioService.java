package com.lilkhalil.music.service;

import com.lilkhalil.music.dao.AudioRepository;
import com.lilkhalil.music.domain.Audio;
import com.lilkhalil.music.dto.UserDTO;
import com.lilkhalil.music.exception.AudioAlreadyExistsException;
import com.lilkhalil.music.exception.AudioNotFoundException;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.Field;
import java.util.List;

@Service
public class AudioService {

    private final static String FILES_API = "http://localhost:8081/api/files/";
    private final static String USER_API = "http://localhost:8080/api/users/";

    @Autowired
    private AudioRepository audioRepository;

    @Autowired
    private RestTemplate restTemplate;

    public Audio addAudio(Audio audio) {
        throwIfExists(audio.getName());

        throwIfUserDoesNotExists(audio.getUserId());

        return audioRepository.saveAndFlush(audio);
    }

    public List<Audio> getAudios() {
        return audioRepository.findAll();
    }

    public Audio getAudioById(Long id) {
        return getAudioIfExists(id);
    }

    public Audio getAudioByName(String name) {
        return getAudioIfExists(name);
    }

    public Audio editAudio(Long id, Audio request) {
        Audio audio = getAudioIfExists(id);
        copyNonNullFields(audio, request);
        return audioRepository.save(audio);
    }

    public Audio uploadImage(Long id, MultipartFile file) {
        Audio audio = getAudioIfExists(id);
        audio.setImageId(uploadFile(file));
        return audioRepository.save(audio);
    }

    public Audio uploadAudio(Long id, MultipartFile file) {
        Audio audio = getAudioIfExists(id);
        audio.setAudioId(uploadFile(file));
        return audioRepository.save(audio);
    }

    public void deleteAudioById(Long id) {
        Audio audio = getAudioIfExists(id);

        restTemplate.delete(FILES_API + audio.getImageId());

        restTemplate.delete(FILES_API + audio.getAudioId());

        audioRepository.deleteById(id);
    }

    private HttpEntity<?> prepareRequest(MultipartFile file) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", file.getResource());
        return new HttpEntity<>(body, headers);
    }

    private String uploadFile(MultipartFile file) {
        return restTemplate.postForObject(FILES_API, prepareRequest(file), String.class);
    }

    private Audio getAudioIfExists(Long id) {
        return audioRepository.findById(id)
                .orElseThrow(() -> new AudioNotFoundException("Audio with id=%d not found!".formatted(id)));
    }

    private Audio getAudioIfExists(String name) {
        return audioRepository.findByName(name)
                .orElseThrow(() -> new AudioNotFoundException("Audio with id=%s not found!".formatted(name)));
    }

    private void throwIfExists(String name) {
        if (audioRepository.findByName(name).isPresent())
            throw new AudioAlreadyExistsException("Audio with name=%s already exists!".formatted(name));
    }

    private void throwIfUserDoesNotExists(Long id) {
        UserDTO user = restTemplate.getForObject(USER_API + id, UserDTO.class);
        if (user == null) {
            throw new RuntimeException("User with id=%d not found!".formatted(id));
        }
    }

    @SneakyThrows
    private void copyNonNullFields(Audio copyTo, Audio copyFrom) {
        Field[] fields = copyTo.getClass().getDeclaredFields();
        for (var field : fields) {
            field.setAccessible(true);
            Object val = field.get(copyFrom);
            if (val != null)
                field.set(copyTo, val);
        }
    }

}
