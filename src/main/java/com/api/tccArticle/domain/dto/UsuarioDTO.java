package com.api.tccArticle.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UsuarioDTO(
        @JsonProperty("id") String cdUsuario,
        @JsonProperty("name") String nmUsuario
) {}
