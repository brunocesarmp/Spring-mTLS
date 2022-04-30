package dev.brunocesar.mtlsclient.controller.dto;

import java.time.LocalDateTime;

public class TestPostRequestDto {

    private final String value;
    private final String from;
    private final LocalDateTime currentDate;

    public TestPostRequestDto() {
        this.value = "Any value here";
        this.from = "mtlsClient";
        this.currentDate = LocalDateTime.now();
    }

    public String getValue() {
        return value;
    }

    public String getFrom() {
        return from;
    }

    public LocalDateTime getCurrentDate() {
        return currentDate;
    }

}
