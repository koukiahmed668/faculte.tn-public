import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminmajorComponent } from './adminmajor.component';

describe('AdminmajorComponent', () => {
  let component: AdminmajorComponent;
  let fixture: ComponentFixture<AdminmajorComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [AdminmajorComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(AdminmajorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
