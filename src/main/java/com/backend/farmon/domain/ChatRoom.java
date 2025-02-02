package com.backend.farmon.domain;

import com.backend.farmon.domain.commons.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@DynamicInsert
@DynamicUpdate
@Entity
public class ChatRoom extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // pk

    @Column(columnDefinition = "TINYINT(1) DEFAULT 0", nullable = false)
    private Boolean isFarmerComplete; // 농업인 거래 여부

    @Column(columnDefinition = "TINYINT(1) DEFAULT 0", nullable = false)
    private Boolean isExpertComplete; // 전문가 거래 여부

    private LocalDateTime farmerLastEnter; // 농업인 마지막 접속 시간

    private LocalDateTime expertLastEnter; // 전문가 마지막 접속 시간

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List <ChatMessage> messageList = new ArrayList<>(); // 채팅 메시지와 일대다 양방향

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "farmer_id", nullable = false)
    private User farmer; // 농업인과 다대일

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "expert_id", nullable = false)
    private Expert expert; // 전문가와 다대일

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estimate_id", nullable = false)
    private Estimate estimate; // 견적과 다대일

//    public void setFarmer(User farmer){
//        if(this.farmer!=null)
//            farmer.getChatRoomList().remove(this);
//
//        this.farmer = farmer;
//
//        farmer.getChatRoomList().add(this);
//    }

//    public void setExpert(Expert expert){
//        if(this.expert!=null)
//            expert.getUser().getChatRoomList().remove(this);
//
//        this.expert = expert;
//
//        expert.getUser().getChatRoomList().add(this);
//    }

//        public void setEstimate(Estimate estimate){
//        if(this.estimate!=null)
//            estimate.getChatRoomList().remove(this);
//
//        this.estimate = estimate;
//
//        estimate.getChatRoomList().add(this);
//    }

}
