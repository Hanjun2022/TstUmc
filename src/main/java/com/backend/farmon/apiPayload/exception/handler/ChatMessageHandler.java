package com.backend.farmon.apiPayload.exception.handler;

import com.backend.farmon.apiPayload.code.BaseErrorCode;
import com.backend.farmon.apiPayload.exception.GeneralException;

public class ChatMessageHandler extends GeneralException  {
    public ChatMessageHandler(BaseErrorCode code) {
        super(code);
    }
}
