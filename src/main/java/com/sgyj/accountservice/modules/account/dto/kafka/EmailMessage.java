package com.sgyj.accountservice.modules.account.dto.kafka;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmailMessage {

    private String to;

    private String subject;

    private String message;

    private EmailMessage(String to, String subject, String message) {
        this.to = to;
        this.subject = subject;
        this.message = message;
    }

    public static EmailMessage of(String to, String subject, String message) {
        return new EmailMessage(to, subject, message);
    }

}
