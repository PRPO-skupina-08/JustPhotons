import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OrganisationButtonComponent } from './organisation-button.component';

describe('OrganisationButtonComponent', () => {
  let component: OrganisationButtonComponent;
  let fixture: ComponentFixture<OrganisationButtonComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [OrganisationButtonComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(OrganisationButtonComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
