package com.synclyplatform.synclyprojectbackend.utils;

import com.synclyplatform.synclyprojectbackend.dto.post.*;
import com.synclyplatform.synclyprojectbackend.model.like.UserPostLike;
import com.synclyplatform.synclyprojectbackend.model.post.*;
import com.synclyplatform.synclyprojectbackend.model.post_collection.PostCollection;
import com.synclyplatform.synclyprojectbackend.model.tag.Tag;
import com.synclyplatform.synclyprojectbackend.model.user.User;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.xml.stream.events.Comment;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public abstract class PostMapper {

    @Autowired
    private PostMapperHelper postMapperHelper;

    @Mapping(source = "id", target = "id")
    @Mapping(source = "createdAt", target = "createdAt")
    @Mapping(source = "updatedAt", target = "updatedAt")
    @Mapping(source = "author.userId", target = "authorId")
    @Mapping(source = "author.username", target = "authorUsername")
    @Mapping(source = "author.userProfile.displayName", target = "authorName")
    @Mapping(source = "postType", target = "postType")
    @Mapping(source = "tags", target = "tags", qualifiedByName = "mapTagsToNames")
    @Mapping(source = "likes", target = "likesBy", qualifiedByName = "mapLikesToUserIds")
    @Mapping(source = "savedByUsers", target = "savedBy", qualifiedByName = "mapSavedByToUserIds")
    @Mapping(source = "id", target = "commentsCount", qualifiedByName = "countPostComments")
    protected abstract void mapBaseFields(Post post, @MappingTarget PostDTO dto);

    public TextPostDTO toDto(TextPost post) {
        TextPostDTO dto = new TextPostDTO();

        mapBaseFields(post, dto);
        dto.setTitle(post.getTitle());
        dto.setContent(post.getContent());

        return dto;
    }

    public QuotePostDTO toDto(QuotePost post) {
        QuotePostDTO dto = new QuotePostDTO();

        mapBaseFields(post, dto);
        dto.setQuote(post.getQuote());
        dto.setSource(post.getSource());

        return dto;
    }

    public PhotoPostDTO toDto(PhotoPost post) {
        PhotoPostDTO dto = new PhotoPostDTO();

        mapBaseFields(post, dto);
        dto.setCaption(post.getCaption());
        List<String> imageUrls = post.getImages().stream()
                .map(image -> {
                    if (image.getUrl() != null) {
                        return image.getUrl();
                    } else {
                        return Base64.getEncoder().encodeToString(image.getImageData());
                    }
                })
                .toList();
        dto.setImageUrls(imageUrls);

        return dto;
    }

    public VideoPostDTO toDto(VideoPost post) {
        VideoPostDTO dto = new VideoPostDTO();

        mapBaseFields(post, dto);
        dto.setDescription(post.getDescription());

        List<String> videoUrls = post.getVideos().stream()
                .map(video -> {
                    if (video.getUrl() != null) {
                        return video.getUrl();
                    } else {
                        return Base64.getEncoder().encodeToString(video.getVideoData());
                    }
                })
                .toList();
        dto.setVideoUrls(videoUrls);

        return dto;
    }

    public LinkPostDTO toDto(LinkPost post) {
        LinkPostDTO dto = new LinkPostDTO();

        mapBaseFields(post, dto);
        dto.setDescription(post.getDescription());
        dto.setTitle(post.getTitle());
        dto.setUrls(post.getLinks());

        return dto;
    }

    @Named("mapTagsToNames")
    public List<String> mapTagsToNames(List<Tag> tags) {
        if (tags == null) return null;
        return tags.stream()
                .map(Tag::getName)
                .collect(Collectors.toList());
    }

    @Named("mapLikesToUserIds")
    public List<Long> mapLikesToUserIds(List<UserPostLike> likes) {
        if (likes == null) return List.of();
        return likes.stream()
                .map(userPostLike -> userPostLike.getUser().getUserId())
                .collect(Collectors.toList());
    }

    @Named("mapSavedByToUserIds")
    public List<Long> mapSavedByToUserIds(Set<User> savedByUsers) {
        if (savedByUsers == null) return List.of();

        return savedByUsers.stream().map(User::getUserId).collect(Collectors.toList());
    }

    @Named("countPostComments")
    public long countPostComments(Long id) {
        return postMapperHelper.countPostComments(id);
    }

    @Mapping(target = "tags", ignore = true)
    public abstract TextPost fromRequestDto(TextPostRequestDTO dto);

    @Mapping(target = "tags", ignore = true)
    public abstract QuotePost fromRequestDto(QuotePostRequestDTO dto);

    @Mapping(target = "tags", ignore = true)
    public abstract PhotoPost fromRequestDto(PhotoPostRequestDTO dto);

    @Mapping(target = "tags", ignore = true)
    public abstract VideoPost fromRequestDto(VideoPostRequestDTO dto);

    @Mapping(target = "tags", ignore = true)
    public abstract LinkPost fromRequestDto(LinkPostRequestDTO dto);


    @AfterMapping
    protected void mapTags(TextPostRequestDTO dto, @MappingTarget TextPost post) {
        post.setTags(postMapperHelper.mapTagNamesToTags(dto.getTags()));
    }

    @AfterMapping
    protected void mapTags(QuotePostRequestDTO dto, @MappingTarget QuotePost post) {
        post.setTags(postMapperHelper.mapTagNamesToTags(dto.getTags()));
    }

    @AfterMapping
    protected void mapTags(PhotoPostRequestDTO dto, @MappingTarget PhotoPost post) {
        post.setTags(postMapperHelper.mapTagNamesToTags(dto.getTags()));
    }

    @AfterMapping
    protected void mapTags(VideoPostRequestDTO dto, @MappingTarget VideoPost post) {
        post.setTags(postMapperHelper.mapTagNamesToTags(dto.getTags()));
    }

    @AfterMapping
    protected void mapTags(LinkPostRequestDTO dto, @MappingTarget LinkPost post) {
        post.setTags(postMapperHelper.mapTagNamesToTags(dto.getTags()));
    }

    @Named("toStringDate")
    public String toStringDate(LocalDateTime time) {
        return time != null ? time.toString() : null;
    }


    public PostDTO toDto(Post post) {
        return switch (post) {
            case TextPost textPost -> toDto(textPost);
            case PhotoPost photoPost -> toDto(photoPost);
            case VideoPost videoPost -> toDto(videoPost);
            case QuotePost quotePost -> toDto(quotePost);
            case LinkPost linkPost -> toDto(linkPost);
            default -> throw new IllegalArgumentException("Unsupported post type: " + post.getClass().getSimpleName());
        };
    }

}
