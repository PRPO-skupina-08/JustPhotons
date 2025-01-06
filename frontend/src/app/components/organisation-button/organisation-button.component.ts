import { Component, Input } from '@angular/core';
import { OrganisationEssentials } from '../../classes/organisation-essentials';

@Component({
  selector: 'app-organisation-button',
  standalone: true,
  imports: [],
  templateUrl: './organisation-button.component.html',
  styleUrl: './organisation-button.component.css'
})
export class OrganisationButtonComponent {
  @Input() organisation!: OrganisationEssentials
}
