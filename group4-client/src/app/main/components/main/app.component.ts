import { Component, OnInit } from '@angular/core';
import { NavigationEnd, Router, RouterOutlet } from '@angular/router';
import { SidebarComponent } from '../sidebar/sidebar.component';
import { filter } from 'rxjs/operators';
import { CommonModule } from '@angular/common';
import { FooterComponent } from '../footer/footer.component';
import { ToastComponent } from '../toast/toast.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [
    CommonModule,
    RouterOutlet,
    SidebarComponent,
    FooterComponent,
    ToastComponent
  ],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent implements OnInit {
  showSidebar: boolean = true;

  constructor(private router: Router) {}

  ngOnInit(): void {
    this.router.events.pipe(
      filter(event => event instanceof NavigationEnd)
    ).subscribe(() => {
      const currentRoute = this.router.routerState.root.firstChild;
      if (currentRoute) {
        this.showSidebar = currentRoute.snapshot.data['header'] !== false;
      }
    });
  }
}
