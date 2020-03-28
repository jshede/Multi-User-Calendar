import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MonthViewEventComponent } from './event.component';

describe('MonthViewEventComponent', () => {
  let component: MonthViewEventComponent;
  let fixture: ComponentFixture<MonthViewEventComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MonthViewEventComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MonthViewEventComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
