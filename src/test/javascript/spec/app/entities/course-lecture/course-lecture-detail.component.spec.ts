import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { KampusTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { CourseLectureDetailComponent } from '../../../../../../main/webapp/app/entities/course-lecture/course-lecture-detail.component';
import { CourseLectureService } from '../../../../../../main/webapp/app/entities/course-lecture/course-lecture.service';
import { CourseLecture } from '../../../../../../main/webapp/app/entities/course-lecture/course-lecture.model';

describe('Component Tests', () => {

    describe('CourseLecture Management Detail Component', () => {
        let comp: CourseLectureDetailComponent;
        let fixture: ComponentFixture<CourseLectureDetailComponent>;
        let service: CourseLectureService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [KampusTestModule],
                declarations: [CourseLectureDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    CourseLectureService,
                    JhiEventManager
                ]
            }).overrideTemplate(CourseLectureDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(CourseLectureDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CourseLectureService);
        });


        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new CourseLecture(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.courseLecture).toEqual(jasmine.objectContaining({id:10}));
            });
        });
    });

});
