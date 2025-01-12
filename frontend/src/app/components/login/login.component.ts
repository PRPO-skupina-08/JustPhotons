import { Component } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { ActivatedRoute, Router } from '@angular/router'
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

interface LoginFormModel {
  email: string;
  password: string;
}

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
  protected email: string = "";
  protected password : string = "";
  protected errorMessage: string = "";

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private authService: AuthService
  ) {}

  onSubmit() {

    if (!this.email || !this.password) {
      return;
    }

    this.errorMessage = "";

    const credentials: LoginFormModel = {
      email: this.email,
      password: this.password
    }

    this.authService
      .login({
        email: credentials.email,
        password: credentials.password,
      })
      .subscribe({
        next: () => {
          const navigateTo = this.route.snapshot.queryParams['returnUrl'] || '/';
          this.router.navigateByUrl(navigateTo);
        },
        error: (error : Error) => {
          // TODO: handle error, maybe show the message?
          this.errorMessage = error.message;
          console.log('login failed', error);
        },
      });
  }

}
