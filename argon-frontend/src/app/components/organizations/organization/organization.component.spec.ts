import { MatIconModule } from '@angular/material/icon';
import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OrganizationComponent } from './organization.component';
import { By } from '@angular/platform-browser';

describe('OrganizationComponent', () => {
  let component: OrganizationComponent;
  let fixture: ComponentFixture<OrganizationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        MatIconModule
      ],
      declarations: [
        OrganizationComponent
      ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(OrganizationComponent);
    component = fixture.componentInstance;
  });

  it('should create component for adding new organization', () => {
    component.organization = {
      name: 'Create organization',
      addOrganizationCard: true
    };

    fixture.detectChanges();

    expect(component).toBeTruthy();
    const iconEl = fixture.debugElement.query(By.css('#logo > mat-icon'));
    expect(iconEl.nativeElement.textContent).toBe('add');
  });

  it('should create component for displaying organization', () => {
    component.organization = {
      name: 'My organization',
      addOrganizationCard: false
    };

    fixture.detectChanges();

    expect(component).toBeTruthy();
    const iconEl = fixture.debugElement.query(By.css('#logo > mat-icon'));
    const titleEl = fixture.debugElement.query(By.css('#name'));
    expect(iconEl.nativeElement.textContent).toBe('business');
    expect(titleEl.nativeElement.textContent).toBe('My organization');
  });

  it('should emit empty event when component is pressed', () => {
    component.organization = {
      name: 'My organization',
      addOrganizationCard: false
    };
    fixture.detectChanges();
    let emitted = false;
    component.onOrganizationClicked.subscribe(() => emitted = true);

    const organizationEl = fixture.debugElement.query(By.css('.organization'));
    organizationEl.nativeElement.click();
    expect(emitted).toBeTrue();
  });
});
