package com.officeknowledgemap.repository;

import com.officeknowledgemap.model.Skill;
import com.officeknowledgemap.model.SkillProficiency;
import com.officeknowledgemap.model.SkillType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SkillRepository extends JpaRepository<Skill, Long> {
    List<Skill> findByUserId(Long userId);
    List<Skill> findByType(SkillType type);
    List<Skill> findByProficiency(SkillProficiency proficiency);
    List<Skill> findByNameContainingIgnoreCase(String name);
    Optional<Skill> findByUserIdAndName(Long userId, String name);
    boolean existsByUserIdAndName(Long userId, String name);
    
    // Delete method for cascading user deletion
    void deleteByUserId(Long userId);
    
    @Query("SELECT s FROM Skill s WHERE s.user.mainTeam.id = :teamId OR s.user.subTeam.id = :teamId")
    List<Skill> findByTeamId(@Param("teamId") Long teamId);
    
    @Query("SELECT s.name, COUNT(s) as count FROM Skill s GROUP BY s.name ORDER BY count DESC")
    List<Object[]> findTopSkills();
    
    @Query("SELECT s FROM Skill s WHERE " +
           "(:name IS NULL OR LOWER(s.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
           "(:type IS NULL OR s.type = :type) AND " +
           "(:proficiency IS NULL OR s.proficiency = :proficiency) AND " +
           "(:userId IS NULL OR s.user.id = :userId)")
    List<Skill> searchSkills(@Param("name") String name, 
                            @Param("type") SkillType type,
                            @Param("proficiency") SkillProficiency proficiency,
                            @Param("userId") Long userId);
}
