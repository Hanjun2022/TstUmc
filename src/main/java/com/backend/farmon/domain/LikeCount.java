package com.backend.farmon.domain;

import com.backend.farmon.domain.commons.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Entity
@Getter
@Setter
@AllArgsConstructor
@Builder
public class LikeCount extends BaseEntity {

    // update가 필요한가?

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    //게시글과의 관계
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    //사용자랑 과의 관계
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name="좋아요 누른 시간")
    private LocalDateTime likeTime;


    public LikeCount(Post post, User user) {
        this.post = post;
        this.user = user;
    }

    public LikeCount() {

    }
}
