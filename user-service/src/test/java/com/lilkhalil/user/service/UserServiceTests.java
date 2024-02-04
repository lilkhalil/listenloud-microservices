package com.lilkhalil.user.service;

import com.lilkhalil.user.dao.UserRepository;
import com.lilkhalil.user.domain.User;
import com.lilkhalil.user.controller.request.RegistrationRequest;
import com.lilkhalil.user.exception.UserAlreadyExistsException;
import com.lilkhalil.user.exception.UserNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private UserService userService;

    @Test
    void createUser_UserDoesNotExists_ExpectedUser() {
        var request = new RegistrationRequest("example", "1234");
        when(userRepository.findByUsername(request.getUsername())).thenReturn(Optional.empty());

        when(userRepository.saveAndFlush(any()))
                .thenReturn(
                        User.builder()
                                .id(anyLong())
                                .username(request.getUsername())
                                .password(request.getPassword())
                                .build()
                );

        User user = userService.createUser(request);

        assertNotNull(user.getId());
        assertEquals(request.getUsername(), user.getUsername());
        assertEquals(request.getPassword(), user.getPassword());
        assertNull(user.getBiography());
        assertNull(user.getImageId());
    }

    @Test
    void createUser_UserAlreadyExists_ThrowException() {
        var request = new RegistrationRequest("example", "1234");
        when(userRepository.findByUsername(request.getUsername())).thenReturn(Optional.of(new User()));

        Executable when = () -> userService.createUser(request);

        assertThrows(UserAlreadyExistsException.class, when);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void readAllUsers_Any_VerifyMock() {
        List<User> expected = List.of(new User(), new User());
        when(userRepository.findAll()).thenReturn(expected);

        List<User> users = userService.readAllUsers();

        assertEquals(expected.size(), users.size());
    }

    @Test
    void readUserById_UserExists_VerifyMock() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User()));
        assertDoesNotThrow(() -> userService.readUserById(1L));
    }

    @Test
    void readUserById_UserDoesNotExists_ThrowException() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> userService.readUserById(1L));
    }

    @Test
    void updateProfileImage_UserExists_ExpectedValue() {
        String objectId = "507f1f77bcf86cd799439011";
        User expected = User.builder()
                .id(1L)
                .username("example")
                .password("1234")
                .biography("Hello, World!")
                .build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(expected));
        when(restTemplate.postForObject(anyString(), any(), any())).thenReturn(objectId);

        expected.setImageId(objectId);

        when(userRepository.save(any())).thenReturn(expected);

        User user = userService.uploadProfileImage(1L, new MockMultipartFile("file", new byte[]{1}));

        assertEquals(expected, user);
    }

    @Test
    void deleteUserById_UserExists_VerifyDeletion() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User()));

        userService.deleteUserById(1L);

        verify(userRepository, times(1)).deleteById(anyLong());
    }

    @Test
    void deleteUserById_UserDoesNotExists_ThrowException() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        Executable when = () -> userService.deleteUserById(1L);

        assertThrows(UserNotFoundException.class, when);
        verifyNoMoreInteractions(userRepository);
    }

}
