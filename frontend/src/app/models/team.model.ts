export interface Team {
  id?: number;
  name: string;
  description?: string;
  parentTeamId?: number;
  isMainTeam: boolean;
  createdAt?: Date | string;
  updatedAt?: Date | string;
}

export interface TeamHierarchy {
  id?: number;
  name: string;
  description?: string;
  isMainTeam: boolean;
  memberCount?: number;
  subTeams: TeamHierarchy[];
}
