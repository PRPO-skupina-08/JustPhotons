import { Component } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { ActivatedRoute, Router } from '@angular/router'
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './register.component.html',
  styleUrl: './register.component.css'
})
export class RegisterComponent {
  protected email: string = "";
  protected username: string = "";
  protected password1 : string = "";
  protected password2 : string = "";
  protected errorMessage: string = "";

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private authService: AuthService
  ) {}

  onSubmit() {

    if (!this.email || !this.password1 || !this.password2 || !this.username  ) {
      return;
    }

    this.errorMessage = "";

    if (this.password1 !== this.password2) {
      this.errorMessage = "Gesli se ne ujemata";
      return;
    }

    this.authService
      .register({
        email: this.email,
        username: this.username,
        password: this.password1,
      })
      .subscribe({
        next: () => {
          const navigateTo = this.route.snapshot.queryParams['returnUrl'] || '/';
          this.router.navigateByUrl(navigateTo);
        },
        error: (error : Error) => {
          // TODO: handle error, maybe show the message?
          this.errorMessage = error.message;
          console.log('registration failed', error);
        },
      });
  }

}
