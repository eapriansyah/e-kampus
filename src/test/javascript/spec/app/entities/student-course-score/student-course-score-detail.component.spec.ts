import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { KampusTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { StudentCourseScoreDetailComponent } from '../../../../../../main/webapp/app/entities/student-course-score/student-course-score-detail.component';
import { StudentCourseScoreService } from '../../../../../../main/webapp/app/entities/student-course-score/student-course-score.service';
import { StudentCourseScore } from '../../../../../../main/webapp/app/entities/student-course-score/student-course-score.model';

describe('Component Tests', () => {

    describe('StudentCourseScore Management Detail Component', () => {
        let comp: StudentCourseScoreDetailComponent;
        let fixture: ComponentFixture<StudentCourseScoreDetailComponent>;
        let service: StudentCourseScoreService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [KampusTestModule],
                declarations: [StudentCourseScoreDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    StudentCourseScoreService,
                    JhiEventManager
                ]
            }).overrideTemplate(StudentCourseScoreDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(StudentCourseScoreDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(StudentCourseScoreService);
        });


        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new StudentCourseScore(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.studentCourseScore).toEqual(jasmine.objectContaining({id:10}));
            });
        });
    });

});
