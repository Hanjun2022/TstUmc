package com.backend.farmon.apiPayload.exception.handler;

import com.backend.farmon.apiPayload.code.BaseErrorCode;
import com.backend.farmon.apiPayload.exception.GeneralException;

public class PostTypeHandler extends GeneralException  {
    public PostTypeHandler(BaseErrorCode code) {
        super(code);
    }

}
