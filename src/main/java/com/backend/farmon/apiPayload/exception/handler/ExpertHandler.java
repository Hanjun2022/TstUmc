package com.backend.farmon.apiPayload.exception.handler;

import com.backend.farmon.apiPayload.code.BaseErrorCode;
import com.backend.farmon.apiPayload.exception.GeneralException;

public class ExpertHandler extends GeneralException {
    public ExpertHandler(BaseErrorCode code) {
        super(code);
    }
}
