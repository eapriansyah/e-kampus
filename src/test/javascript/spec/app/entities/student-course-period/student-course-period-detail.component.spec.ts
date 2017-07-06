import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { KampusTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { StudentCoursePeriodDetailComponent } from '../../../../../../main/webapp/app/entities/student-course-period/student-course-period-detail.component';
import { StudentCoursePeriodService } from '../../../../../../main/webapp/app/entities/student-course-period/student-course-period.service';
import { StudentCoursePeriod } from '../../../../../../main/webapp/app/entities/student-course-period/student-course-period.model';

describe('Component Tests', () => {

    describe('StudentCoursePeriod Management Detail Component', () => {
        let comp: StudentCoursePeriodDetailComponent;
        let fixture: ComponentFixture<StudentCoursePeriodDetailComponent>;
        let service: StudentCoursePeriodService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [KampusTestModule],
                declarations: [StudentCoursePeriodDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    StudentCoursePeriodService,
                    JhiEventManager
                ]
            }).overrideTemplate(StudentCoursePeriodDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(StudentCoursePeriodDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(StudentCoursePeriodService);
        });


        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new StudentCoursePeriod(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.studentCoursePeriod).toEqual(jasmine.objectContaining({id:10}));
            });
        });
    });

});
