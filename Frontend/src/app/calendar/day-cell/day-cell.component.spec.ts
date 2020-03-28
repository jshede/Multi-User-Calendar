import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MonthViewDayCellComponent } from './day-cell.component';

describe('MonthViewDayCellComponent', () => {
  let component: MonthViewDayCellComponent;
  let fixture: ComponentFixture<MonthViewDayCellComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MonthViewDayCellComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MonthViewDayCellComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
