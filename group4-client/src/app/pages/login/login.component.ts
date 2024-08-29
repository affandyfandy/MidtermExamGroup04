import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {first} from 'rxjs/operators';
import {ActivatedRoute, Router} from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    ReactiveFormsModule,
  ],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent implements OnInit {

  public form: FormGroup = this.fb.group({});  // {1}
  private formSubmitAttempt: boolean = false; // {2}

  returnUrl: string;

  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute, // {3}
    private router: Router,
    private authService: AuthService // {4}
  ) {
    // redirect to home if already logged in
    // if (this.authService.currentUserValue) {
    //   this.router.navigate(['/']);
    // }

    this.returnUrl = '/product';
  }

  ngOnInit() {
    this.form = this.fb.group({     // {5}
      userName: ['', Validators.required],
      password: ['', Validators.required]
    });
    // get return url from route parameters or default to '/'
    this.returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/product';
  }

  // isFieldInvalid(field: string) { // {6}
  //   return (
  //     (!this.form.get(field).valid && this.form.get(field).touched) ||
  //     (this.form.get(field).untouched && this.formSubmitAttempt)
  //   );
  // }

  onSubmit() {
    if (this.form.valid) {
      let username = this.form.get('userName');
      let password = this.form.get('password');
      if(username != null && password != null){
        this.authService.login(username.value, password.value)
        .pipe(first())
        .subscribe(
          data => {
            this.router.navigate([this.returnUrl]);
          },
          error => {
            // this.error = error;
            // this.loading = false;
          });
      }

    }
    this.formSubmitAttempt = true;             // {8}
  }

}
