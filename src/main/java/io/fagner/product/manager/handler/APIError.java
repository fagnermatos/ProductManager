package io.fagner.product.manager.handler;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

@Builder
public record APIError (
    String message,
    Integer code
) {}