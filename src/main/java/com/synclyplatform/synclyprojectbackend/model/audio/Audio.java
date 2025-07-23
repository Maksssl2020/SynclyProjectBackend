package com.synclyplatform.synclyprojectbackend.model.audio;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Audio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @URL(message = "Please provide a valid URL.")
    private String url;

    @Column(columnDefinition = "bytea")
    private byte[] audioData;

    private String mimeType;
}
