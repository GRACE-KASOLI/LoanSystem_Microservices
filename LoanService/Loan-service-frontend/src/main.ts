import { bootstrapApplication } from '@angular/platform-browser';
import { AppComponent } from './app/app.component';
import { routes } from './app/app.routes';
import { importProvidersFrom } from '@angular/core'; // ✅ Add this import
import { HttpClientModule } from '@angular/common/http'; // ✅ Add
import { provideRouter } from '@angular/router';
import { provideHttpClient } from '@angular/common/http';
bootstrapApplication(AppComponent, {
  providers: [
    provideRouter(routes),
     importProvidersFrom(HttpClientModule), // ✅ Add this line
    provideHttpClient() // Register HttpClient for Standalone Components
  ],
}).catch(err => console.error(err));
