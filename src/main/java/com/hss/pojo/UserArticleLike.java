package com.hss.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserArticleLike {
    private Integer userId;
    private Integer articleId;
    private Boolean isLiked;
}
