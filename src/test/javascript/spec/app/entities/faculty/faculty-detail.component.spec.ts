import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { KampusTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { FacultyDetailComponent } from '../../../../../../main/webapp/app/entities/faculty/faculty-detail.component';
import { FacultyService } from '../../../../../../main/webapp/app/entities/faculty/faculty.service';
import { Faculty } from '../../../../../../main/webapp/app/entities/faculty/faculty.model';

describe('Component Tests', () => {

    describe('Faculty Management Detail Component', () => {
        let comp: FacultyDetailComponent;
        let fixture: ComponentFixture<FacultyDetailComponent>;
        let service: FacultyService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [KampusTestModule],
                declarations: [FacultyDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    FacultyService,
                    JhiEventManager
                ]
            }).overrideTemplate(FacultyDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(FacultyDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(FacultyService);
        });


        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Faculty(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.faculty).toEqual(jasmine.objectContaining({id:10}));
            });
        });
    });

});
