export enum UserRole {
  ADMIN = 'ADMIN',
  MANAGER = 'MANAGER',
  TEAM_LEAD = 'TEAM_LEAD',
  EMPLOYEE = 'EMPLOYEE'
}

export interface User {
  id?: number;
  username: string;
  password?: string;
  name: string;
  role: UserRole;
  contactInfo: string;
  photoUrl?: string;
  mainTeamId?: number;
  subTeamId?: number;
  mainTeamName?: string;
  subTeamName?: string;
  createdAt?: Date | string;
  updatedAt?: Date | string;
}
