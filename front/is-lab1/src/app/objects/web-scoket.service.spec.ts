import { TestBed } from '@angular/core/testing';

import { WebScoketService } from './web-scoket.service';

describe('WebScoketService', () => {
  let service: WebScoketService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(WebScoketService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
