package com.backend.farmon.domain;

import com.backend.farmon.domain.commons.BaseEntity;
import com.backend.farmon.dto.Filter.FieldCategory;
import com.backend.farmon.dto.Filter.FieldCategoryEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Table(name="post")
@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="post_id")
    private Long id;

    private String postTitle;

    private String postContent;

  //  private PostType postType;

    // 어느 게시판에 추가
    @ManyToOne
    @JoinColumn(name="board_id")
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    @JsonBackReference
    private User user;

    @Column(columnDefinition = "INTEGER DEFAULT 0", nullable = false)
    private int postLikes;



    // 상위 카테고리만 저장
    @Enumerated(EnumType.STRING)
    @Column(name="field_category", nullable = false)
    private FieldCategory fieldCategory;


    @ElementCollection
    @CollectionTable(name = "post_selected_sub_categories", joinColumns = @JoinColumn(name = "post_id"))
    @Column(name = "sub_category_name")
    private List<String> selectedSubCategories = new ArrayList<>();




    // 좋아요 순(Qureydsl로 가져오는걸로 가능)
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LikeCount> postlikes = new ArrayList<>();

    //댓글 갯수 저장
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    // 답변 가져오기
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Answer> answers = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL,orphanRemoval = true)
    @JsonManagedReference
    private List<PostImg> postImgs = new ArrayList<>();

    public Post() {

    }

    public void increaseLikes() {
        this.postLikes++;
    }

    public void decreaseLikes() {
        this.postLikes--;
    }

    public int getLikeCount() {
        return this.postlikes.size();
    }

}
