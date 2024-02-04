package com.lilkhalil.user.controller.request;

import com.lilkhalil.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class RegistrationRequest {

    protected String username;
    protected String password;
    public User build(User user) {
        user.setUsername(username);
        user.setPassword(password);
        return user;
    }

}
