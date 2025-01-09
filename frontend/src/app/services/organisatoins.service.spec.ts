import { TestBed } from '@angular/core/testing';

import { OrganisatoinsService } from './organisatoins.service';

describe('OrganisatoinsServiceService', () => {
  let service: OrganisatoinsService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(OrganisatoinsService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
