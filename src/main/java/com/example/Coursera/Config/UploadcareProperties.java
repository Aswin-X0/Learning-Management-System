package com.example.Coursera.Config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
@ConfigurationProperties(prefix = "uploadcare")
public class UploadcareProperties {

    private String publicKey;
    private String secretKey;
}
