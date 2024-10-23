import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ObjectLineComponent } from './object-line.component';

describe('ObjectLineComponent', () => {
  let component: ObjectLineComponent;
  let fixture: ComponentFixture<ObjectLineComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ObjectLineComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ObjectLineComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
