export enum SkillType {
  TECHNICAL = 'TECHNICAL',
  SOFT_SKILL = 'SOFT_SKILL',
  DOMAIN_KNOWLEDGE = 'DOMAIN_KNOWLEDGE',
  CERTIFICATION = 'CERTIFICATION'
}

export enum SkillProficiency {
  BEGINNER = 'BEGINNER',
  INTERMEDIATE = 'INTERMEDIATE',
  ADVANCED = 'ADVANCED',
  EXPERT = 'EXPERT'
}

export interface Skill {
  id?: number;
  userId?: number;
  userName?: string;
  name: string;
  type: SkillType;
  proficiency: SkillProficiency;
  description?: string;
  yearsOfExperience?: number;
  endorsementCount?: number;
  createdAt?: Date | string;
  updatedAt?: Date | string;
}

export interface SkillWithEndorsements extends Skill {
  endorsements: any[]; // Will be populated with actual endorsement data
}
