package com.backend.farmon.apiPayload.exception.handler;

import com.backend.farmon.apiPayload.code.BaseErrorCode;
import com.backend.farmon.apiPayload.exception.GeneralException;

public class AreaHandler extends GeneralException {
    public AreaHandler(BaseErrorCode code) { super(code); }

}
