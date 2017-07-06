import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { KampusTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { StudentPeriodsDetailComponent } from '../../../../../../main/webapp/app/entities/student-periods/student-periods-detail.component';
import { StudentPeriodsService } from '../../../../../../main/webapp/app/entities/student-periods/student-periods.service';
import { StudentPeriods } from '../../../../../../main/webapp/app/entities/student-periods/student-periods.model';

describe('Component Tests', () => {

    describe('StudentPeriods Management Detail Component', () => {
        let comp: StudentPeriodsDetailComponent;
        let fixture: ComponentFixture<StudentPeriodsDetailComponent>;
        let service: StudentPeriodsService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [KampusTestModule],
                declarations: [StudentPeriodsDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    StudentPeriodsService,
                    JhiEventManager
                ]
            }).overrideTemplate(StudentPeriodsDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(StudentPeriodsDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(StudentPeriodsService);
        });


        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new StudentPeriods(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.studentPeriods).toEqual(jasmine.objectContaining({id:10}));
            });
        });
    });

});
