import { SkillType, SkillProficiency } from './skill.model';

export interface TopSkill {
  skillName: string;
  skillType: SkillType;
  userCount: number;
  totalEndorsements: number;
  averageProficiency: string;
}

export interface EndorsementTrend {
  date: string;
  count: number;
}

export interface TeamSkillSummary {
  teamId: number;
  teamName: string;
  totalMembers: number;
  totalSkills: number;
  topSkills: {
    skillName: string;
    count: number;
  }[];
  skillDistribution: {
    skillType: SkillType;
    count: number;
  }[];
  proficiencyDistribution: {
    proficiency: SkillProficiency;
    count: number;
  }[];
}

export interface AnalyticsDashboard {
  topSkills: TopSkill[];
  endorsementTrends: EndorsementTrend[];
  teamSummaries: TeamSkillSummary[];
  totalUsers: number;
  totalSkills: number;
  totalEndorsements: number;
}
