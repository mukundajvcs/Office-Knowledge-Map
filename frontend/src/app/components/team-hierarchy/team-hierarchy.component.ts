import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatTreeModule, MatTreeNestedDataSource } from '@angular/material/tree';
import { NestedTreeControl } from '@angular/cdk/tree';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatBadgeModule } from '@angular/material/badge';
import { TeamService } from '../../services/team.service';
import { TeamHierarchy } from '../../models/team.model';

@Component({
  selector: 'app-team-hierarchy',
  standalone: true,
  imports: [
    CommonModule,
    MatTreeModule,
    MatIconModule,
    MatButtonModule,
    MatCardModule,
    MatProgressSpinnerModule,
    MatBadgeModule
  ],
  templateUrl: './team-hierarchy.component.html',
  styleUrls: ['./team-hierarchy.component.scss']
})
export class TeamHierarchyComponent implements OnInit {
  treeControl = new NestedTreeControl<TeamHierarchy>(node => node.subTeams);
  dataSource = new MatTreeNestedDataSource<TeamHierarchy>();
  isLoading = false;
  isExpanded = true; // Track if tree is expanded or collapsed

  constructor(
    private teamService: TeamService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.loadHierarchy();
  }

  loadHierarchy(): void {
    this.isLoading = true;
    this.teamService.getTeamHierarchy().subscribe({
      next: (hierarchy) => {
        console.log('Hierarchy data received:', hierarchy);
        this.dataSource.data = hierarchy;
        this.isLoading = false;
        console.log('Data source updated:', this.dataSource.data);
        // Manually trigger change detection
        this.cdr.detectChanges();
        // Expand all nodes by default after the tree is rendered
        setTimeout(() => {
          this.expandAll();
        }, 100);
      },
      error: (error) => {
        console.error('Failed to load team hierarchy', error);
        this.isLoading = false;
        this.cdr.detectChanges();
      }
    });
  }

  hasChild = (_: number, node: TeamHierarchy) => 
    !!node.subTeams && node.subTeams.length > 0;

  expandAll(): void {
    // Recursively expand all nodes
    this.expandAllRecursive(this.dataSource.data);
    this.isExpanded = true;
    this.cdr.detectChanges();
  }

  private expandAllRecursive(nodes: TeamHierarchy[]): void {
    nodes.forEach(node => {
      this.treeControl.expand(node);
      if (node.subTeams && node.subTeams.length > 0) {
        this.expandAllRecursive(node.subTeams);
      }
    });
  }

  collapseAll(): void {
    this.treeControl.collapseAll();
    this.isExpanded = false;
    this.cdr.detectChanges();
  }

  refreshHierarchy(): void {
    const currentState = this.isExpanded;
    this.loadHierarchy();
    // Restore the state after loading
    setTimeout(() => {
      if (!currentState) {
        this.collapseAll();
      }
    }, 150);
  }
}
