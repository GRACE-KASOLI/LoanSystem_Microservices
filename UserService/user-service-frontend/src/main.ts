import { bootstrapApplication } from '@angular/platform-browser';
import { provideRouter } from '@angular/router';
import { provideHttpClient } from '@angular/common/http';
import { routes, appProviders } from './app/app.routes';
import { AppComponent } from './app/app.component';

console.log('✅ BootstrapApplication: Initializing with provideHttpClient()');

bootstrapApplication(AppComponent, {
  providers: [provideRouter(routes), provideHttpClient(), ...appProviders],
});
