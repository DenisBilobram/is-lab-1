import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ObjectsCreatorComponent } from './objects-creator.component';

describe('ObjectsCreatorComponent', () => {
  let component: ObjectsCreatorComponent;
  let fixture: ComponentFixture<ObjectsCreatorComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ObjectsCreatorComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ObjectsCreatorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
