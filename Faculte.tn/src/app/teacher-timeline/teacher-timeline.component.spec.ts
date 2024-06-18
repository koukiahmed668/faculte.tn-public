import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TeacherTimelineComponent } from './teacher-timeline.component';

describe('TeacherTimelineComponent', () => {
  let component: TeacherTimelineComponent;
  let fixture: ComponentFixture<TeacherTimelineComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [TeacherTimelineComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(TeacherTimelineComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
