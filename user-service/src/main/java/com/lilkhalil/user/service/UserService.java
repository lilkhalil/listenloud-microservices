package com.lilkhalil.user.service;

import com.lilkhalil.user.dao.UserRepository;
import com.lilkhalil.user.domain.User;
import com.lilkhalil.user.dto.RegistrationRequest;
import com.lilkhalil.user.dto.UpdateRequest;
import com.lilkhalil.user.exception.UserAlreadyExistsException;
import com.lilkhalil.user.exception.UserNotFoundException;
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

import java.util.List;

@Service
public class UserService {

    private final static String URL = "http://localhost:8081/api/files";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RestTemplate restTemplate;

    public User createUser(RegistrationRequest request) {

        if (userRepository.findByUsername(request.getUsername()).isPresent())
            throw new UserAlreadyExistsException("User with this username already exists!");

        User user = User.builder()
                .username(request.getUsername())
                .password(request.getPassword())
                .build();

        userRepository.saveAndFlush(user);

        return user;
    }

    public List<User> readAllUsers() {
        return userRepository.findAll();
    }

    public User readUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User with this id not found!"));
    }

    public User readUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User with this username not found!"));
    }

    public User updateUser(Long id, UpdateRequest request) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with this id not found!"));

        user.setBiography(request.getBiography());
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());

        userRepository.save(user);

        return user;
    }

    @SneakyThrows
    public User uploadProfileImage(Long id, MultipartFile file) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User does not found!"));

        String response = restTemplate.postForObject(URL, prepareRequest(file), String.class);

        user.setImageId(response);

        userRepository.save(user);

        return user;
    }

    public void deleteUserById(Long id) {

        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User with this id not found!"));

        restTemplate.delete(URL + user.getImageId());

        userRepository.deleteById(id);
    }

    private HttpEntity<?> prepareRequest(MultipartFile file) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", file.getResource());
        return new HttpEntity<>(body, headers);
    }

}
