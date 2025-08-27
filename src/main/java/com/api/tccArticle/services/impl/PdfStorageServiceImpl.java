package com.api.tccArticle.services.impl;

import com.api.tccArticle.services.PdfStorageService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class PdfStorageServiceImpl implements PdfStorageService {
    @Override
    public String save(MultipartFile file) {
        try {
            String fileName = UUID.randomUUID() + "-" + file.getOriginalFilename();
            Path path = Paths.get("uploads/" + fileName);
            Files.createDirectories(path.getParent());
            file.transferTo(path);
            return path.toString();
        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar o arquivo PDF", e);
        }
    }
}
