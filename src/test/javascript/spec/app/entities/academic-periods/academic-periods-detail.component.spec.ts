import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { KampusTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { AcademicPeriodsDetailComponent } from '../../../../../../main/webapp/app/entities/academic-periods/academic-periods-detail.component';
import { AcademicPeriodsService } from '../../../../../../main/webapp/app/entities/academic-periods/academic-periods.service';
import { AcademicPeriods } from '../../../../../../main/webapp/app/entities/academic-periods/academic-periods.model';

describe('Component Tests', () => {

    describe('AcademicPeriods Management Detail Component', () => {
        let comp: AcademicPeriodsDetailComponent;
        let fixture: ComponentFixture<AcademicPeriodsDetailComponent>;
        let service: AcademicPeriodsService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [KampusTestModule],
                declarations: [AcademicPeriodsDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    AcademicPeriodsService,
                    JhiEventManager
                ]
            }).overrideTemplate(AcademicPeriodsDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(AcademicPeriodsDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(AcademicPeriodsService);
        });


        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new AcademicPeriods(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.academicPeriods).toEqual(jasmine.objectContaining({id:10}));
            });
        });
    });

});
