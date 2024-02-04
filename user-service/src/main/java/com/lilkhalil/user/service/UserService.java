package com.lilkhalil.user.service;

import com.lilkhalil.user.controller.request.RegistrationRequest;
import com.lilkhalil.user.controller.request.UpdateRequest;
import com.lilkhalil.user.dao.UserRepository;
import com.lilkhalil.user.domain.User;
import com.lilkhalil.user.exception.UserAlreadyExistsException;
import com.lilkhalil.user.exception.UserNotFoundException;
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

    private final static String URL = "http://localhost:8081/api/files/";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RestTemplate restTemplate;

    public User createUser(RegistrationRequest request) {

        throwIfUserExists(request.getUsername());

        User user = request.build(new User());

        return userRepository.saveAndFlush(user);
    }

    public List<User> readAllUsers() {
        return userRepository.findAll();
    }

    public User readUserById(Long id) {
        return getUserIfExists(id);
    }

    public User readUserByUsername(String username) {
        return getUserIfExists(username);
    }

    public User updateUser(Long id, UpdateRequest request) {

        User user = request.build(getUserIfExists(id));

        return userRepository.save(user);
    }

    public User uploadProfileImage(Long id, MultipartFile file) {

        User user = getUserIfExists(id);

        String response = restTemplate.postForObject(URL, prepareRequest(file), String.class);

        user.setImageId(response);

        return userRepository.save(user);
    }

    public void deleteUserById(Long id) {

        User user = getUserIfExists(id);

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

    private User getUserIfExists(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User with username=%s not found!".formatted(username)));
    }

    private User getUserIfExists(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with id=%d not found!".formatted(id)));
    }

    private void throwIfUserExists(String username) {
        if (userRepository.findByUsername(username).isPresent())
            throw new UserAlreadyExistsException("User with username=%s already exists!".formatted(username));
    }

}
