package com.example.banan.service.impl;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Base64;

@Service
public class UtilService {

    public String getEncode(File file) throws IOException {

        String encodedString = Base64.getEncoder().encodeToString(FileUtils.readFileToByteArray(file));

        return encodedString;

    }

}
