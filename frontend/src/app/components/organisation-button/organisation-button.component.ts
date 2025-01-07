import { Component, Input } from '@angular/core';
import { OrganisationEssentials } from '../../classes/organisation-essentials';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-organisation-button',
  standalone: true,
  imports: [RouterLink],
  templateUrl: './organisation-button.component.html',
  styleUrl: './organisation-button.component.css'
})
export class OrganisationButtonComponent {
  @Input() organisation!: OrganisationEssentials
}
