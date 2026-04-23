export interface CsvUploadResult {
  success: boolean;
  totalRows: number;
  successCount: number;
  failedCount: number;
  errors: string[];
}

export interface UserCsvRow {
  username: string;
  password: string;
  name: string;
  role: string;
  contactInfo: string;
  mainTeam?: string;
  subTeam?: string;
}
