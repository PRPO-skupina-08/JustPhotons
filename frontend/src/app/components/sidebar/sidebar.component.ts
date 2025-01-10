import { Component, OnInit } from '@angular/core';
import { OrganisationButtonComponent } from '../organisation-button/organisation-button.component';
import { OrganisationEssentials } from '../../classes/organisation-essentials'
import { CommonModule } from '@angular/common';
import { OrganisatoinsService } from '../../services/organisatoins.service';

@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [OrganisationButtonComponent, CommonModule],
  templateUrl: './sidebar.component.html',
  styleUrl: './sidebar.component.css'
})
export class SidebarComponent implements OnInit {
  constructor(
    private readonly organisationsService: OrganisatoinsService
  ) {}

  ngOnInit(): void {
    this.getAllOrganisations();
  }

  protected organisations : OrganisationEssentials[] = [];
  // = 
  // [
  //   {
  //     id: 1,
  //     name: 'Mladi SDS',
  //   },
  //   {
  //     id: 2,
  //     name: 'Skavti Ljubljana',
  //   },
  //   {
  //     id: 3,
  //     name: 'Skavti Murska Sobota',
  //   }
  // ]

  protected getAllOrganisations() {
    this.organisationsService.getAllOrganisations().subscribe({
      next: (organisations) => {
        this.organisations = organisations;
      }
    })
  }

  
}
