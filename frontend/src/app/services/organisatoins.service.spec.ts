import { TestBed } from '@angular/core/testing';

import { OrganisatoinsServiceService } from './organisatoins.service';

describe('OrganisatoinsServiceService', () => {
  let service: OrganisatoinsServiceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(OrganisatoinsServiceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
