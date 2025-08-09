package com.synclyplatform.synclyprojectbackend.dto.post;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonTypeInfo(
        use =  JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = TextPostRequestDTO.class, name = "text"),
        @JsonSubTypes.Type(value = QuotePostRequestDTO.class, name = "quote"),
        @JsonSubTypes.Type(value = PhotoPostRequestDTO.class, name = "photo"),
        @JsonSubTypes.Type(value = VideoPostRequestDTO.class, name = "video"),
        @JsonSubTypes.Type(value = LinkPostRequestDTO.class, name = "link")
})
public class PostRequestDTO {
    private List<String> tags;
}
