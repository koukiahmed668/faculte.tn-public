import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CourseProposalFormComponent } from './course-proposal-form.component';

describe('CourseProposalFormComponent', () => {
  let component: CourseProposalFormComponent;
  let fixture: ComponentFixture<CourseProposalFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [CourseProposalFormComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(CourseProposalFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
