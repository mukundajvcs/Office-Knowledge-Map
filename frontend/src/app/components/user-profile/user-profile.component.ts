import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatDividerModule } from '@angular/material/divider';
import { AuthService } from '../../services/auth.service';
import { UserService } from '../../services/user.service';
import { User } from '../../models/user.model';

@Component({
  selector: 'app-user-profile',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule,
    MatSnackBarModule,
    MatProgressSpinnerModule,
    MatDividerModule
  ],
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.scss']
})
export class UserProfileComponent implements OnInit {
  profileForm: FormGroup;
  currentUser: User | null = null;
  isLoading = false;
  isSaving = false;
  selectedFile: File | null = null;
  photoPreview: string | null = null;
  isUploadingPhoto = false;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private userService: UserService,
    private snackBar: MatSnackBar,
    private cdr: ChangeDetectorRef
  ) {
    this.profileForm = this.fb.group({
      name: ['', [Validators.required, Validators.minLength(2)]],
      contactInfo: ['', [Validators.required, Validators.email]],
      username: [{ value: '', disabled: true }],
      role: [{ value: '', disabled: true }]
    });
  }

  ngOnInit(): void {
    console.log('===== UserProfileComponent ngOnInit called =====');
    console.log('isLoading initial state:', this.isLoading);
    this.loadUserProfile();
  }

  loadUserProfile(): void {
    console.log('===== loadUserProfile called =====');
    this.currentUser = this.authService.getCurrentUser();
    console.log('Initial currentUser from auth:', this.currentUser);
    console.log('Does currentUser exist?', !!this.currentUser);
    console.log('Does username exist?', this.currentUser?.username);
    
    if (!this.currentUser || !this.currentUser.username) {
      console.error('No current user found! User might not be logged in.');
      this.isLoading = false;
      console.log('Set isLoading to false (no user)');
      this.snackBar.open('Please login to view your profile', 'Close', { duration: 3000 });
      return;
    }
    
    console.log('Setting isLoading to true...');
    this.isLoading = true;
    console.log('isLoading is now:', this.isLoading);
    
    // If we don't have the user ID, fetch by username first
    if (!this.currentUser.id) {
      console.log('User ID missing, fetching all users to find by username...');
      this.userService.getAllUsers().subscribe({
        next: (users) => {
          console.log('Received users response:', users);
          const foundUser = users.find(u => u.username === this.currentUser?.username);
          if (foundUser) {
            console.log('Found user with ID:', foundUser);
            this.currentUser = foundUser;
            this.populateForm(foundUser);
          } else {
            console.error('User not found in database');
            this.snackBar.open('User not found', 'Close', { duration: 3000 });
          }
          console.log('Setting isLoading to false (getAllUsers)');
          this.isLoading = false;
          this.cdr.detectChanges();
        },
        error: (error) => {
          console.error('Failed to fetch users:', error);
          this.snackBar.open('Failed to load profile', 'Close', { duration: 3000 });
          console.log('Setting isLoading to false (error in getAllUsers)');
          this.isLoading = false;
          this.cdr.detectChanges();
        }
      });
    } else {
      // We have the user ID, fetch directly
      console.log('Fetching user by ID:', this.currentUser.id);
      this.userService.getUserById(this.currentUser.id).subscribe({
        next: (user) => {
          console.log('Fetched user by ID:', user);
          this.currentUser = user;
          this.populateForm(user);
          console.log('Setting isLoading to false (getUserById success)');
          this.isLoading = false;
          this.cdr.detectChanges();
        },
        error: (error) => {
          console.error('Failed to fetch user by ID:', error);
          this.snackBar.open('Failed to load profile', 'Close', { duration: 3000 });
          console.log('Setting isLoading to false (error in getUserById)');
          this.isLoading = false;
          this.cdr.detectChanges();
        }
      });
    }
    
    // Failsafe timeout
    setTimeout(() => {
      if (this.isLoading) {
        console.error('TIMEOUT: Request took too long, forcing isLoading to false');
        this.isLoading = false;
        this.cdr.detectChanges(); // Force change detection
        this.snackBar.open('Request timed out. Please try again.', 'Close', { duration: 3000 });
      }
    }, 10000); // 10 second timeout
  }

  private populateForm(user: User): void {
    console.log('populateForm called with user:', user);
    try {
      this.profileForm.patchValue({
        name: user.name,
        contactInfo: user.contactInfo,
        username: user.username,
        role: user.role
      });
      console.log('Form values patched');
      // Mark the form as pristine and touched to ensure validation works
      this.profileForm.markAsPristine();
      this.profileForm.markAsUntouched();
      this.profileForm.updateValueAndValidity();
      console.log('Form validation updated');
      // Construct full URL for the photo with /api context path
      this.photoPreview = user.photoUrl ? `http://localhost:8080/api${user.photoUrl}` : null;
      console.log('Photo preview set:', this.photoPreview);
    } catch (error) {
      console.error('Error in populateForm:', error);
      throw error;
    }
  }

  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      this.selectedFile = input.files[0];
      
      // Preview the image
      const reader = new FileReader();
      reader.onload = (e: any) => {
        this.photoPreview = e.target.result;
      };
      reader.readAsDataURL(this.selectedFile);
    } else if (!this.selectedFile && this.currentUser?.photoUrl) {
      // If file input was cleared but user has a photo, restore it
      this.photoPreview = `http://localhost:8080/api${this.currentUser.photoUrl}`;
    }
  }

  uploadPhoto(): void {
    if (this.selectedFile && this.currentUser?.id) {
      this.isUploadingPhoto = true;
      this.userService.uploadPhoto(this.currentUser.id, this.selectedFile).subscribe({
        next: (user) => {
          this.snackBar.open('Photo uploaded successfully!', 'Close', { duration: 3000 });
          this.currentUser = user;
          // Construct full URL for the photo with /api context path
          this.photoPreview = user.photoUrl ? `http://localhost:8080/api${user.photoUrl}` : null;
          this.selectedFile = null;
          this.isUploadingPhoto = false;
          // Force change detection to ensure photo displays
          this.cdr.detectChanges();
        },
        error: (error) => {
          this.snackBar.open('Failed to upload photo', 'Close', { duration: 3000 });
          this.isUploadingPhoto = false;
        }
      });
    }
  }

  clearPhoto(): void {
    this.selectedFile = null;
    // Restore original photo preview with full URL and /api context path
    this.photoPreview = this.currentUser?.photoUrl ? `http://localhost:8080/api${this.currentUser.photoUrl}` : null;
  }

  onUpdateProfile(): void {
    console.log('=== Save button clicked! ===');
    console.log('Form valid:', this.profileForm.valid);
    console.log('Form invalid:', this.profileForm.invalid);
    console.log('Form pristine:', this.profileForm.pristine);
    console.log('Form dirty:', this.profileForm.dirty);
    console.log('Form value:', this.profileForm.value);
    console.log('Form getRawValue:', this.profileForm.getRawValue());
    console.log('Current user ID:', this.currentUser?.id);
    
    // Check each control
    console.log('=== Control Status ===');
    Object.keys(this.profileForm.controls).forEach(key => {
      const control = this.profileForm.get(key);
      console.log(`${key}:`, {
        value: control?.value,
        valid: control?.valid,
        invalid: control?.invalid,
        disabled: control?.disabled,
        errors: control?.errors,
        touched: control?.touched,
        dirty: control?.dirty
      });
    });
    
    // Use getRawValue to include disabled fields, but only send editable ones
    if (this.profileForm.valid && this.currentUser?.id) {
      this.isSaving = true;
      const updates = {
        name: this.profileForm.get('name')?.value,
        contactInfo: this.profileForm.get('contactInfo')?.value
      };

      console.log('Sending update request:', updates);
      this.userService.updateUser(this.currentUser.id, updates).subscribe({
        next: (user) => {
          console.log('Update successful:', user);
          this.snackBar.open('Profile updated successfully!', 'Close', { duration: 3000 });
          this.currentUser = user;
          this.profileForm.markAsPristine();
          this.isSaving = false;
        },
        error: (error) => {
          console.error('Update failed:', error);
          this.snackBar.open('Failed to update profile', 'Close', { duration: 3000 });
          this.isSaving = false;
        }
      });
    } else {
      console.error('=== Form is invalid or user ID missing ===');
      if (!this.profileForm.valid) {
        console.error('Form level errors:', this.profileForm.errors);
      }
      if (!this.currentUser?.id) {
        console.error('User ID is missing!');
      }
      this.snackBar.open('Please check all required fields', 'Close', { duration: 3000 });
    }
  }

  getInitials(): string {
    if (this.currentUser?.name) {
      return this.currentUser.name
        .split(' ')
        .map(n => n[0])
        .join('')
        .toUpperCase()
        .slice(0, 2);
    }
    return 'U';
  }

  onImageError(event: Event): void {
    console.error('Image failed to load:', this.photoPreview);
    console.log('Current user photoUrl:', this.currentUser?.photoUrl);
    // Fallback to placeholder if image fails to load
    if (this.currentUser?.photoUrl) {
      console.log('Attempting to restore photo URL');
      this.photoPreview = `http://localhost:8080/api${this.currentUser.photoUrl}`;
    }
  }
}
