import { Component, OnInit } from '@angular/core';
import { ToastService } from '../../../services/toast.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-toast',
  templateUrl: './toast.component.html',
  standalone: true,
  imports: [CommonModule],
  styleUrls: ['./toast.component.scss']
})
export class ToastComponent implements OnInit {
  toast: { message: string, type: 'success' | 'error' | 'warning' } | null = null;

  constructor(private toastService: ToastService) {}

  ngOnInit(): void {
    this.toastService.toast$.subscribe(toast => {
      this.toast = toast;
      if (toast) {
        setTimeout(() => this.clearToast(), 2000);
      }
    });
  }

  clearToast(): void {
    this.toast = null;
  }
}

