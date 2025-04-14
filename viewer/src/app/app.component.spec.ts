import {TestBed} from '@angular/core/testing';
import {AppComponent} from './app.component';
import {FormBuilder, ReactiveFormsModule} from '@angular/forms';
import {initialMapState} from './state/map/map.reducer';
import {provideMockStore} from '@ngrx/store/testing';

describe('AppComponent', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AppComponent],
      providers: [
        FormBuilder,
        ReactiveFormsModule,
        provideMockStore({initialState: initialMapState}),
      ],
    }).compileComponents();
  });

  it('should create the app', () => {
    const fixture = TestBed.createComponent(AppComponent);
    const app = fixture.componentInstance;
    expect(app).toBeTruthy();
  });

  it(`should have the 'viewer' title`, () => {
    const fixture = TestBed.createComponent(AppComponent);
    const app = fixture.componentInstance;
    expect(app.title).toEqual('viewer');
  });

  it('should contains map div', () => {
    const fixture = TestBed.createComponent(AppComponent);
    fixture.detectChanges();
    const compiled = fixture.nativeElement as HTMLElement;
    expect(compiled.querySelector('map'));
  });
});
