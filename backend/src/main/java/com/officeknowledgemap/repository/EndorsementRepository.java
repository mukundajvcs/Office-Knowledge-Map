package com.officeknowledgemap.repository;

import com.officeknowledgemap.model.Endorsement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface EndorsementRepository extends JpaRepository<Endorsement, Long> {
    List<Endorsement> findBySkillId(Long skillId);
    List<Endorsement> findByEndorserId(Long endorserId);
    List<Endorsement> findBySkillUserId(Long userId);
    Optional<Endorsement> findByEndorserIdAndSkillId(Long endorserId, Long skillId);
    boolean existsByEndorserIdAndSkillId(Long endorserId, Long skillId);
    
    // Delete methods for cascading deletion
    void deleteBySkillId(Long skillId);
    void deleteByEndorserId(Long endorserId);
    
    @Query("SELECT COUNT(e) FROM Endorsement e WHERE e.skill.id = :skillId")
    Long countBySkillId(@Param("skillId") Long skillId);
    
    @Query("SELECT COUNT(e) FROM Endorsement e WHERE e.skill.user.id = :userId")
    Long countByUserId(@Param("userId") Long userId);
    
    @Query("SELECT e FROM Endorsement e WHERE e.createdAt >= :startDate")
    List<Endorsement> findRecentEndorsements(@Param("startDate") LocalDateTime startDate);
    
    @Query("SELECT s.name, COUNT(e) as count FROM Endorsement e JOIN e.skill s " +
           "GROUP BY s.name ORDER BY count DESC")
    List<Object[]> findMostEndorsedSkills();
}
