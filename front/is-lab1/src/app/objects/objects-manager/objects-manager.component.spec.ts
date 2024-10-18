import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ObjectsManagerComponent } from './objects-manager.component';

describe('ObjectsManagerComponent', () => {
  let component: ObjectsManagerComponent;
  let fixture: ComponentFixture<ObjectsManagerComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ObjectsManagerComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ObjectsManagerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
