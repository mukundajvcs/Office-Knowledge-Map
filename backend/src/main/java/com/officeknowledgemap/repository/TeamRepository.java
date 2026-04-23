package com.officeknowledgemap.repository;

import com.officeknowledgemap.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {
    List<Team> findByParentTeamIsNull();
    List<Team> findByParentTeamId(Long parentTeamId);
    List<Team> findByIsMainTeam(Boolean isMainTeam);
}
