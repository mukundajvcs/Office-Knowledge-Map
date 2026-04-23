import { UserRole } from './user.model';
import { SkillProficiency, SkillType } from './skill.model';

export interface SearchFilters {
  skillName?: string;
  skillType?: SkillType;
  proficiency?: SkillProficiency;
  mainTeamId?: number;
  subTeamId?: number;
  role?: UserRole;
}

export interface SearchResult {
  userId: number;
  userName: string;
  userRole: UserRole;
  contactInfo: string;
  photoUrl?: string;
  mainTeamId?: number;
  mainTeamName?: string;
  subTeamId?: number;
  subTeamName?: string;
  skills: {
    skillId: number;
    name: string;
    type: SkillType;
    proficiency: SkillProficiency;
    endorsementCount: number;
  }[];
  totalEndorsements: number;
}
