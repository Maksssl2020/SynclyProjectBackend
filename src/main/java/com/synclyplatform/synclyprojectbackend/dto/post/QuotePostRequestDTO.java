package com.synclyplatform.synclyprojectbackend.dto.post;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuotePostRequestDTO extends PostRequestDTO{

    @NotBlank(message = "Quote cannot be empty.")
    private String quote;

    private String source;
}
