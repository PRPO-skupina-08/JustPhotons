import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Organisation } from '../../classes/organisation';
import { OrganisatoinsService } from '../../services/organisatoins.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-organisation',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './organisation.component.html',
  styleUrl: './organisation.component.css'
})

export class OrganisationComponent implements OnInit {
  constructor(
    private route: ActivatedRoute,
    private readonly organisationsService: OrganisatoinsService
  ) {
    this.route.paramMap.subscribe(params => {
      const userId = params.get('id');
      if (userId) {
        this.orgId = parseInt(userId, 10);
      }
      this.ngOnInit();
    });
  }

  protected orgId : number = 0;
  protected organisation!: Organisation;

  ngOnInit() {
    
    this.organisationsService.getOne(this.orgId).subscribe({
      next: (res) => {
        this.organisation = res;
      }
    })
  }

}
