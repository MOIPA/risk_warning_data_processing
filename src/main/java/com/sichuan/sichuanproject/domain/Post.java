package com.sichuan.sichuanproject.domain;

import lombok.Data;

/**
 * @author
 */

@Data
public class Post {
    private Integer autoId;
    private String id;
    private String text;
    private Integer attitudesCount;
    private Integer commentsCount;
    private Integer repostsCount;
    private String createdAt;
    private String theme;
    private String type;
    private Float sentiment;
}
