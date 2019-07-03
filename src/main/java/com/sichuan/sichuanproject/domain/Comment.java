package com.sichuan.sichuanproject.domain;

import lombok.Data;

/**
 * @author
 */

@Data
public class Comment {
    private Integer commentId;
    private Integer postId;
    private String userId;
    private String screenName;
    private String profileUrl;
    private String description;
    private String gender;
    private String text;
    private String createdAt;
    private Integer likeCount;
    private Integer followersCount;
    private Float sentiment;
}
