package com.backend.farmon.apiPayload.exception.handler;

import com.backend.farmon.apiPayload.code.BaseErrorCode;
import com.backend.farmon.apiPayload.exception.GeneralException;

public class EstimateHandler extends GeneralException {
    public EstimateHandler(BaseErrorCode code) { super(code); }

}
