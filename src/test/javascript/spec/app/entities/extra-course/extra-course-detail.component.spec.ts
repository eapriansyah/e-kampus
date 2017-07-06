import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { KampusTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { ExtraCourseDetailComponent } from '../../../../../../main/webapp/app/entities/extra-course/extra-course-detail.component';
import { ExtraCourseService } from '../../../../../../main/webapp/app/entities/extra-course/extra-course.service';
import { ExtraCourse } from '../../../../../../main/webapp/app/entities/extra-course/extra-course.model';

describe('Component Tests', () => {

    describe('ExtraCourse Management Detail Component', () => {
        let comp: ExtraCourseDetailComponent;
        let fixture: ComponentFixture<ExtraCourseDetailComponent>;
        let service: ExtraCourseService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [KampusTestModule],
                declarations: [ExtraCourseDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    ExtraCourseService,
                    JhiEventManager
                ]
            }).overrideTemplate(ExtraCourseDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ExtraCourseDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ExtraCourseService);
        });


        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new ExtraCourse(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.extraCourse).toEqual(jasmine.objectContaining({id:10}));
            });
        });
    });

});
