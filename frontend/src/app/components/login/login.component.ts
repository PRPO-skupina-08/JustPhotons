import { Component } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { ActivatedRoute, Router } from '@angular/router'
import { FormsModule } from '@angular/forms';

interface LoginFormModel {
  email: string;
  password: string;
}

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
  protected email: string = "";
  protected password : string = "";

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private authService: AuthService
  ) {}

  onSubmit() {

    if (!this.email || !this.password) {
      return;
    }

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
        error: (error) => {
          // TODO: handle error, maybe show the message?

          console.log('login failed', error);
        },
      });
  }

}
