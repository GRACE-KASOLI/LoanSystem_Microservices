import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class FileUploadService {
  private uploadUrl = 'http://localhost:9097/api/export/upload'; // Change to your backend URL

  constructor(private http: HttpClient) {
      console.log('FileUploadService initialized'); // ✅ Debugging
}

  uploadFile(file: File): Observable<string> {
    const formData = new FormData();
    formData.append('file', file);

    return this.http.post(this.uploadUrl, formData, { responseType: 'text' });
  }
}
