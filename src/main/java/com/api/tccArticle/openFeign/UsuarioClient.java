package com.api.tccArticle.openFeign;

import com.api.tccArticle.domain.dto.UsuarioDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "usuario-service", url = "http://localhost:8080")
public interface UsuarioClient {

    @GetMapping("/users/{id}")
    UsuarioDTO buscarPorId(@PathVariable String id);
}
