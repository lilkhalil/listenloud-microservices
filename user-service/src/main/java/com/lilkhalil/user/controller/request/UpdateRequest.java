package com.lilkhalil.user.controller.request;

import com.lilkhalil.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UpdateRequest extends RegistrationRequest {

    private String biography;
    private String imageId;

    public User build(User user) {
        super.build(user);
        user.setBiography(biography);
        user.setImageId(imageId);
        return user;
    }

}
