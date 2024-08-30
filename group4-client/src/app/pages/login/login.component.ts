import {Component, ElementRef, OnInit, Renderer2} from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {Router} from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { CommonModule } from '@angular/common';
import { ToastService } from '../../services/toast.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule
  ],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent implements OnInit {

  public loginForm!: FormGroup
  public errorMessage = ''

  constructor(
    private toastService: ToastService,
    private formBuilder: FormBuilder,
    private authService: AuthService,
    private router: Router,
    private renderer: Renderer2,
    private el: ElementRef) {}

    ngOnInit(): void {
      this.loginForm = this.formBuilder.group({
        email: ['', [Validators.required, Validators.email]],
        password: ['', Validators.required]
      })
    }

  /**
   * Handles client login
   * @returns
   */
  handleLogin() {
    if (!this.loginForm.valid) {
      return;
    }

    this.authService.login(this.loginForm.value).subscribe({
      next: (v) => {
        this.router.navigate(['product'])
        this.toastService.showToast('Logged in successfully!', 'success');
      },
      error: (e) => {
        this.errorMessage = (e.error.message ?
          e.error.message : 'Server Error: Please run the server...')
        this.showToast()
        this.toastService.showToast('Failed to login', 'error');
        setTimeout(() => this.hideToast(), 5000);
      }
    });

  }

  private showToast() {
    this.renderer.addClass(this.el.nativeElement.querySelector('#errorToast'), 'show');
  }

  private hideToast() {
    this.renderer.removeClass(this.el.nativeElement.querySelector('#errorToast'), 'show');
  }

}
