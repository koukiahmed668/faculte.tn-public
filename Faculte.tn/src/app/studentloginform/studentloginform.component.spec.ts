import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StudentloginformComponent } from './studentloginform.component';

describe('StudentloginformComponent', () => {
  let component: StudentloginformComponent;
  let fixture: ComponentFixture<StudentloginformComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [StudentloginformComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(StudentloginformComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
