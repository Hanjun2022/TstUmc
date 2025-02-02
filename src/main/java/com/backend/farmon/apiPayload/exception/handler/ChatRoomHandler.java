package com.backend.farmon.apiPayload.exception.handler;

import com.backend.farmon.apiPayload.code.BaseErrorCode;
import com.backend.farmon.apiPayload.exception.GeneralException;

public class ChatRoomHandler extends GeneralException {
    public ChatRoomHandler(BaseErrorCode code) {
        super(code);
    }
}
