package com.api.tccArticle.services;

import org.springframework.web.multipart.MultipartFile;

public interface PdfStorageService {
    String save(MultipartFile file);
}
