package com.synclyplatform.synclyprojectbackend.dto.post;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonTypeInfo(
        use =  JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = TextPostRequestDTO.class, name = "TEXT"),
        @JsonSubTypes.Type(value = QuotePostRequestDTO.class, name = "QUOTE"),
        @JsonSubTypes.Type(value = PhotoPostRequestDTO.class, name = "PHOTO"),
        @JsonSubTypes.Type(value = VideoPostRequestDTO.class, name = "VIDEO"),
        @JsonSubTypes.Type(value = LinkPostRequestDTO.class, name = "LINK")
})
public class PostRequestDTO {
    private Set<String> tags;
}
