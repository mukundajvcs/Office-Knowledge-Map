package com.officeknowledgemap.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "endorsements", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"endorser_id", "skill_id"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Endorsement {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "endorser_id", nullable = false)
    private User endorser;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "skill_id", nullable = false)
    private Skill skill;
    
    private String comment;
    
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
}
