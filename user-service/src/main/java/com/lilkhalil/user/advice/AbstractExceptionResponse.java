package com.lilkhalil.user.advice;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AbstractExceptionResponse {

    private String code;
    private String message;
    private String timestamp;
    private String path;

}
