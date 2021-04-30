package com.genesislab.videoservice.domain.video.exception;

import com.genesislab.videoservice.global.error.exception.EntityNotFoundException;

public class VideoNotFoundException extends EntityNotFoundException {
    public VideoNotFoundException(String message) {
        super(message);
    }
}
