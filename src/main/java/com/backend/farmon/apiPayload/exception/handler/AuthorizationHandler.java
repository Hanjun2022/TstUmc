package com.backend.farmon.apiPayload.exception.handler;

import com.backend.farmon.apiPayload.code.BaseErrorCode;
import com.backend.farmon.apiPayload.exception.GeneralException;

public class AuthorizationHandler extends GeneralException {
    public AuthorizationHandler(BaseErrorCode code) {
        super(code);
    }

}
