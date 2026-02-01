package com.synclyplatform.synclyprojectbackend.dto.authentication;

import lombok.*;

import java.util.List;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TurnstileResponseDTO {

    private Boolean success;
    private List<String> errorCodes;
}

