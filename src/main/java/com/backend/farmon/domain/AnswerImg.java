package com.backend.farmon.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AnswerImg {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="img_id")
    private Long id; // img_no

    private String storedFileName;

    private String originalFileName;

    // Img:Post = N:1
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "answer_id")
    @JsonBackReference
    private Answer answer;

    public void changePost(Answer answer){
        if(this.answer!=null){
            this.answer.getAnswerImgList().remove(this);
        }
        this.answer=answer;
    }
}
