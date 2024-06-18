import { TestBed } from '@angular/core/testing';

import { PaidCourseService } from './paid-course.service';

describe('PaidCourseService', () => {
  let service: PaidCourseService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PaidCourseService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
