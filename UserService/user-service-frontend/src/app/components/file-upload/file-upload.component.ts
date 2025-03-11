import { Component } from '@angular/core';
import { NgIf } from '@angular/common'; // ✅ Import NgIf
import { HttpClient } from '@angular/common/http'; // ✅ Correct import
import { FileUploadService } from '../../services/file-upload.service';

@Component({
  selector: 'app-file-upload',
  standalone: true, // ✅ Ensure this is a standalone component
  imports: [NgIf], // ✅ Add NgIf here to fix the error
  templateUrl: './file-upload.component.html',
  styleUrls: ['./file-upload.component.css']
})
export class FileUploadComponent {
  selectedFile: File | null = null;
  uploadMessage: string = '';

  constructor(private fileUploadService: FileUploadService) {}

  onFileSelected(event: any) {
    if (event.target.files.length > 0) {
      this.selectedFile = event.target.files[0];
    }
  }

  uploadFile() {
    if (!this.selectedFile) {
      this.uploadMessage = 'Please select a file first.';
      return;
    }

    this.fileUploadService.uploadFile(this.selectedFile).subscribe({
      next: (response) => (this.uploadMessage = response),
      error: (error) => (this.uploadMessage = 'Error: ' + error.error)
    });
  }
}
