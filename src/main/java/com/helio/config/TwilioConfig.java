package com.helio.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "twilio")
@Data
public class TwilioConfig {

    private String accountSid;
    private String authToken;
    private String senderMobileNumber;
}