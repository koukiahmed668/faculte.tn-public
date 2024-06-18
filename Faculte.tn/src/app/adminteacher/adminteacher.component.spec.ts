import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminteacherComponent } from './adminteacher.component';

describe('AdminteacherComponent', () => {
  let component: AdminteacherComponent;
  let fixture: ComponentFixture<AdminteacherComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [AdminteacherComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(AdminteacherComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
