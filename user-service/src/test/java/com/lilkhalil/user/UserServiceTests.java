package com.lilkhalil.user;

import com.lilkhalil.user.dao.UserRepository;
import com.lilkhalil.user.domain.User;
import com.lilkhalil.user.dto.RegistrationRequest;
import com.lilkhalil.user.dto.UpdateRequest;
import com.lilkhalil.user.exception.UserAlreadyExistsException;
import com.lilkhalil.user.exception.UserNotFoundException;
import com.lilkhalil.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@SpringBootTest
public class UserServiceTests {

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private RestTemplate restTemplate;

    @Autowired
    private UserService userService;

    @Test
    void whenUserAlreadyExistsThenThrowException() {
        given(userRepository.findByUsername("example")).willReturn(Optional.of(
                new User(1L, "example", "1234", "Hello, World!", null)
        ));
        var request = new RegistrationRequest("example", "124");
        assertThrows(UserAlreadyExistsException.class, () -> userService.createUser(request));
    }

    @Test
    void whenUserNotFoundThenThrowException() {
        given(userRepository.findById(1L)).willReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> userService.readUserById(1L));
    }

    @Test
    void whenUserCreatedThenExpectUser() {
        User user = User.builder().username("example").password("1234").build();
        given(userRepository.saveAndFlush(user)).willReturn(user);
        var request = new RegistrationRequest("example", "1234");
        assertEquals(userService.createUser(request), user);
    }

    @Test
    void whenReadAllUsersThenExpectListOfSameLengthReturned() {
        given(userRepository.findAll()).willReturn(List.of(new User(), new User()));
        assertEquals(userService.readAllUsers().size(), 2);
    }

    @Test
    void whenReadUserByUsernameThenExpectEqualUsername() {
        User user = User.builder().username("example").build();
        given(userRepository.findByUsername("example")).willReturn(Optional.of(user));
        assertEquals(userService.readUserByUsername("example"), user);
    }

    @Test
    void whenUserUpdatesProfileImageThenExpectEqualObjectId() {
        MultipartFile file = new MockMultipartFile("example.png", new byte[]{1, 2, 3, 4, 5});
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", file.getResource());
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(body, headers);
        given(userRepository.findById(1L)).willReturn(Optional.of(User.builder().id(1L).build()));
        given(restTemplate.postForObject("http://localhost:8081/api/files", request, String.class)).willReturn("1");
        assertEquals(userService.uploadProfileImage(1L, file).getImageId(), "1");
    }

    @Test
    void whenUserFieldsUpdatesThenExceptEqualUserFields() {
        User user = User.builder().id(1L).build();
        given(userRepository.findById(1L)).willReturn(Optional.of(user));
        var request = new UpdateRequest("example", "pass", "Hello World!");
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        user.setBiography(request.getBiography());
        assertEquals(userService.updateUser(1L, request), user);
    }

    @Test
    void whenUserWithThisIdNotExistsThenExpectExceptionThrown() {
        given(userRepository.findById(1L)).willReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> userService.deleteUserById(1L));
    }

}
