import { Component, OnInit } from '@angular/core';
import { OrganisationButtonComponent } from '../organisation-button/organisation-button.component';
import { OrganisationEssentials } from '../../classes/organisation-essentials'
import { CommonModule } from '@angular/common';
import { OrganisatoinsService } from '../../services/organisatoins.service';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [OrganisationButtonComponent, CommonModule],
  templateUrl: './sidebar.component.html',
  styleUrl: './sidebar.component.css'
})
export class SidebarComponent implements OnInit {
  constructor(
    private readonly organisationsService: OrganisatoinsService,
    private readonly authService: AuthService
  ) {}

  ngOnInit(): void {
    if (this.authService.isLoggedIn()) {
      this.getAllOrganisations();
    }
  }

  protected organisations : OrganisationEssentials[] = [];

  protected getAllOrganisations() {
    this.organisationsService.getAllOrganisations().subscribe({
      next: (organisations) => {
        this.organisations = organisations;
      }
    })
  }

  
}
