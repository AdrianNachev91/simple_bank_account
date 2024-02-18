package com.bank.bank_account.utils;

import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;

import static java.nio.charset.StandardCharsets.UTF_8;

public class TestUtils {

    @SneakyThrows
    public static String extractJson(String resourcePath) {
        var requestResource = new ClassPathResource(resourcePath);
        return IOUtils.toString(requestResource.getInputStream(), UTF_8);
    }
}
