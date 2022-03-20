import { TestBed } from '@angular/core/testing';

import { WorktimeService } from './worktime.service';

describe('WorktimeService', () => {
  let service: WorktimeService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(WorktimeService);
  });

  xit('should be created', () => {
    expect(service).toBeTruthy();
  });
});
