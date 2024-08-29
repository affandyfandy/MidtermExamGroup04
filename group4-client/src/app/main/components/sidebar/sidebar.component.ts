import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { AppComponent } from '../main/app.component';
import { AppConstants } from '../../../config/app.constants';

@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.scss'],
  standalone: true,
  imports: [CommonModule, RouterModule]
})
export class SidebarComponent {
  isCollapsed = false;

  constructor(
    private router: Router
  ) { }

  toggleCollapse(): void {
    this.isCollapsed = !this.isCollapsed;
  }

  logout() {
    if (typeof window !== 'undefined') {
      localStorage.removeItem(AppConstants.LOCALSTORAGE_LOGIN_ACCESS_TOKEN);
    }
    this.router.navigate(['/login']).then(() => {
      console.log('Logged out and redirected to login page.');
    }).catch(err => {
      console.error('Navigation error:', err);
    });
  }
}
