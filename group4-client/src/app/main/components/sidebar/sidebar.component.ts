import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { AppComponent } from '../main/app.component';
import { AppConstants } from '../../../config/app.constants';
import { ToastService } from '../../../services/toast.service';

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
    private router: Router,
    private toastService: ToastService
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
      this.toastService.showToast('Logged out and redirected to login page.', 'success');
    }).catch(err => {
      console.error('Navigation error:', err);
      this.toastService.showToast('Error to logged out', 'error');
    });
  }
}
