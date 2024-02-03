package com.lilkhalil.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lilkhalil.user.controller.UserController;
import com.lilkhalil.user.domain.User;
import com.lilkhalil.user.dto.RegistrationRequest;
import com.lilkhalil.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerTests {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    UserService userService;

    @Test
    void whenGetUserThenExpectEqualArgs() throws Exception {
        given(userService.readUserById(1L)).willReturn(User.builder().id(1L).username("example").password("123").build());
        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.username").value("example"))
                .andExpect(jsonPath("$.password").value("123"));
    }

    @Test
    void whenGetAllUsersThenExpectEqualListLength() throws Exception {
        given(userService.readAllUsers()).willReturn(List.of(new User(), new User()));
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void whenCreateUserSuccessThenExpectStatusCreated() throws Exception {
        var request = new RegistrationRequest("example", "123");
        given(userService.createUser(request)).willReturn(User.builder().username("example").password("123").build());
        mockMvc.perform(
                post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("example"))
                .andExpect(jsonPath("$.password").value("123"));
    }

}
