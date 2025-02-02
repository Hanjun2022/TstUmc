package com.backend.farmon.strategy.postType;

import com.backend.farmon.domain.Post;
import com.backend.farmon.dto.post.PostType;

import java.util.List;

public interface PostFetchStrategy {
    List<Post> fetchPosts(PostType postType);
}
