import { TestBed } from '@angular/core/testing';

import { PaidCourseEnrollmentService } from './paid-course-enrollment.service';

describe('PaidCourseEnrollmentService', () => {
  let service: PaidCourseEnrollmentService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PaidCourseEnrollmentService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
