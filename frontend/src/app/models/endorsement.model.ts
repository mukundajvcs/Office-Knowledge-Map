export interface Endorsement {
  id?: number;
  endorserId: number;
  endorserName?: string;
  endorsedUserId: number;
  endorsedUserName?: string;
  skillId: number;
  skillName?: string;
  timestamp?: Date | string;
  createdAt?: Date | string;
}

export interface EndorsementSummary {
  skillId: number;
  skillName: string;
  totalEndorsements: number;
  endorsers: {
    id: number;
    name: string;
    timestamp: Date | string;
  }[];
}

export interface UserEndorsementGroup {
  userId: number;
  userName: string;
  skills: {
    skillId: number;
    skillName: string;
    endorsementDate: Date | string;
  }[];
}
