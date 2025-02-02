package com.backend.farmon.apiPayload.exception.handler;

import com.backend.farmon.apiPayload.code.BaseErrorCode;
import com.backend.farmon.apiPayload.exception.GeneralException;

public class SearchHandler extends GeneralException {
    public SearchHandler(BaseErrorCode code) {
        super(code);
    }

}
