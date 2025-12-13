package com.clapped.main.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProcessResult {
    private boolean success;
    private String message;

    public static ProcessResult success(String message) {
        log.info(message);
        return new ProcessResult(true, message);
    }

    public static ProcessResult error(String message) {
        log.warn(message);
        return new ProcessResult(false, "ERROR: " + message);
    }
}

