import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatTableModule } from '@angular/material/table';
import { MatChipsModule } from '@angular/material/chips';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatDividerModule } from '@angular/material/divider';
import { MatTabsModule } from '@angular/material/tabs';
import { MatMenuModule } from '@angular/material/menu';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { AnalyticsService } from '../../services/analytics.service';
import { AuthService } from '../../services/auth.service';
import { AnalyticsDashboard, TopSkill, TeamSkillSummary } from '../../models/analytics.model';

@Component({
  selector: 'app-analytics-dashboard',
  standalone: true,
  imports: [
    CommonModule,
    MatCardModule,
    MatIconModule,
    MatButtonModule,
    MatTableModule,
    MatChipsModule,
    MatProgressSpinnerModule,
    MatDividerModule,
    MatTabsModule,
    MatMenuModule,
    MatSnackBarModule
  ],
  templateUrl: './analytics-dashboard.component.html',
  styleUrls: ['./analytics-dashboard.component.scss']
})
export class AnalyticsDashboardComponent implements OnInit {
  dashboardData: AnalyticsDashboard | null = null;
  isLoading = false;
  isExporting = false;
  canViewAnalytics = false;

  topSkillsColumns: string[] = ['skillName', 'skillType', 'userCount', 'endorsements', 'proficiency'];

  constructor(
    private analyticsService: AnalyticsService,
    private authService: AuthService,
    private snackBar: MatSnackBar,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.canViewAnalytics = this.authService.isManagerOrAbove();
    if (this.canViewAnalytics) {
      this.loadDashboard();
    }
  }

  loadDashboard(): void {
    this.isLoading = true;
    this.analyticsService.getDashboard().subscribe({
      next: (data) => {
        this.dashboardData = data;
        this.isLoading = false;
        // Manually trigger change detection to update UI
        this.cdr.detectChanges();
        console.log('Analytics data loaded:', data);
      },
      error: (error) => {
        this.snackBar.open('Failed to load analytics', 'Close', { duration: 3000 });
        this.isLoading = false;
        this.cdr.detectChanges();
      }
    });
  }

  exportSkillsData(): void {
    this.isExporting = true;
    this.analyticsService.exportSkillsData().subscribe({
      next: (blob) => {
        this.downloadFile(blob, 'skills-export.csv');
        this.snackBar.open('Skills data exported successfully', 'Close', { duration: 3000 });
        this.isExporting = false;
      },
      error: (error) => {
        this.snackBar.open('Failed to export skills data', 'Close', { duration: 3000 });
        this.isExporting = false;
      }
    });
  }

  exportEndorsementsData(): void {
    this.isExporting = true;
    this.analyticsService.exportEndorsementsData().subscribe({
      next: (blob) => {
        this.downloadFile(blob, 'endorsements-export.csv');
        this.snackBar.open('Endorsements data exported successfully', 'Close', { duration: 3000 });
        this.isExporting = false;
      },
      error: (error) => {
        this.snackBar.open('Failed to export endorsements data', 'Close', { duration: 3000 });
        this.isExporting = false;
      }
    });
  }

  exportAllData(): void {
    this.isExporting = true;
    this.analyticsService.exportAllData().subscribe({
      next: (blob) => {
        this.downloadFile(blob, 'complete-export.csv');
        this.snackBar.open('All data exported successfully', 'Close', { duration: 3000 });
        this.isExporting = false;
      },
      error: (error) => {
        this.snackBar.open('Failed to export data', 'Close', { duration: 3000 });
        this.isExporting = false;
      }
    });
  }

  private downloadFile(blob: Blob, filename: string): void {
    const url = window.URL.createObjectURL(blob);
    const link = document.createElement('a');
    link.href = url;
    link.download = filename;
    link.click();
    window.URL.revokeObjectURL(url);
  }

  refreshDashboard(): void {
    this.loadDashboard();
  }

  getSkillTypeClass(skillType: string): string {
    return `skill-type-${skillType.toLowerCase()}`;
  }

  getProficiencyStars(avgProficiency: string): string {
    if (avgProficiency.includes('ADVANCED')) return '⭐⭐⭐';
    if (avgProficiency.includes('INTERMEDIATE')) return '⭐⭐';
    return '⭐';
  }

  getTeamColor(index: number): string {
    const colors = ['#667eea', '#764ba2', '#f093fb', '#4facfe', '#43e97b'];
    return colors[index % colors.length];
  }
}
