import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TeacherloginformComponent } from './teacherloginform.component';

describe('TeacherloginformComponent', () => {
  let component: TeacherloginformComponent;
  let fixture: ComponentFixture<TeacherloginformComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [TeacherloginformComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(TeacherloginformComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
