package com.backend.farmon.domain;

import com.backend.farmon.domain.commons.BaseEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
        import lombok.*;

@Table(name = "PostImg")
@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostImg extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="img_id")
    private Long id; // img_no

    private String storedFileName;

    private String originalFileName;

    // Img:Post = N:1
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    @JsonBackReference
    private Post post;

    public void changePost(Post post){
        if(this.post!=null){
            this.post.getPostImgs().remove(this);
        }
        this.post = post;
//        post.getPostImgs().add(this);
    }

}
