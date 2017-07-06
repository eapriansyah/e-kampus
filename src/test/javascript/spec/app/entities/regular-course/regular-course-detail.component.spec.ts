import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { KampusTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { RegularCourseDetailComponent } from '../../../../../../main/webapp/app/entities/regular-course/regular-course-detail.component';
import { RegularCourseService } from '../../../../../../main/webapp/app/entities/regular-course/regular-course.service';
import { RegularCourse } from '../../../../../../main/webapp/app/entities/regular-course/regular-course.model';

describe('Component Tests', () => {

    describe('RegularCourse Management Detail Component', () => {
        let comp: RegularCourseDetailComponent;
        let fixture: ComponentFixture<RegularCourseDetailComponent>;
        let service: RegularCourseService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [KampusTestModule],
                declarations: [RegularCourseDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    RegularCourseService,
                    JhiEventManager
                ]
            }).overrideTemplate(RegularCourseDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(RegularCourseDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(RegularCourseService);
        });


        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new RegularCourse(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.regularCourse).toEqual(jasmine.objectContaining({id:10}));
            });
        });
    });

});
