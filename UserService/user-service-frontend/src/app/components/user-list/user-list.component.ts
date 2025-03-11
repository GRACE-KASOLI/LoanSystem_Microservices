import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-user-list',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './user-list.component.html',
  styleUrls: ['./user-list.component.css'],
})
export class UserListComponent implements OnInit {
  users: any[] = [];
  editingUserId: number | null = null; // Track the user being edited
  apiUrl = 'http://localhost:9097/users';
  private http = inject(HttpClient);

  ngOnInit() {
    this.fetchUsers();
  }

  fetchUsers() {
    this.http.get<any[]>(this.apiUrl).subscribe({
      next: (data) => (this.users = data),
      error: (err) => console.error('Error fetching users:', err),
    });
  }

  deleteUser(id: number) {
    if (confirm('Are you sure you want to delete this user?')) {
      this.http.delete(`${this.apiUrl}/${id}`).subscribe({
        next: () => {
          this.users = this.users.filter(user => user.id !== id);
          console.log('User deleted successfully');
        },
        error: (err) => console.error('Error deleting user:', err),
      });
    }
  }

  startEdit(userId: number) {
    this.editingUserId = userId; // Set the user ID to be edited
  }

  cancelEdit() {
    this.editingUserId = null; // Reset editing state
  }

  updateUser(user: any) {
    this.http.put(`${this.apiUrl}/${user.id}`, user).subscribe({
      next: (updatedUser) => {
        this.users = this.users.map(u => u.id === user.id ? updatedUser : u);
        this.editingUserId = null; // Reset editing state
        console.log('User updated successfully');
      },
      error: (err) => console.error('Error updating user:', err),
    });
  }
}
