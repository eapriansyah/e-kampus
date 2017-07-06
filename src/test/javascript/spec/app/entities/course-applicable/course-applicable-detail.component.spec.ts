import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { KampusTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { CourseApplicableDetailComponent } from '../../../../../../main/webapp/app/entities/course-applicable/course-applicable-detail.component';
import { CourseApplicableService } from '../../../../../../main/webapp/app/entities/course-applicable/course-applicable.service';
import { CourseApplicable } from '../../../../../../main/webapp/app/entities/course-applicable/course-applicable.model';

describe('Component Tests', () => {

    describe('CourseApplicable Management Detail Component', () => {
        let comp: CourseApplicableDetailComponent;
        let fixture: ComponentFixture<CourseApplicableDetailComponent>;
        let service: CourseApplicableService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [KampusTestModule],
                declarations: [CourseApplicableDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    CourseApplicableService,
                    JhiEventManager
                ]
            }).overrideTemplate(CourseApplicableDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(CourseApplicableDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CourseApplicableService);
        });


        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new CourseApplicable(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.courseApplicable).toEqual(jasmine.objectContaining({id:10}));
            });
        });
    });

});
