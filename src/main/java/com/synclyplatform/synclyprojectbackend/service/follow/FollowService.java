package com.synclyplatform.synclyprojectbackend.service.follow;

import com.synclyplatform.synclyprojectbackend.dto.tag.TagDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Service
public interface FollowService {

    List<TagDTO> getFollowedTags(Long userId);
    void followTag(Long userId, Long tagId);
    void unfollowTag(Long userId, Long tagId);
}
