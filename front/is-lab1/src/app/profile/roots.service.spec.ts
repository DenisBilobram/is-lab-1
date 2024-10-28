import { TestBed } from '@angular/core/testing';

import { RootsService } from './roots.service';

describe('RootsService', () => {
  let service: RootsService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(RootsService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
