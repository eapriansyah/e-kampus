import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { KampusTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { LectureDetailComponent } from '../../../../../../main/webapp/app/entities/lecture/lecture-detail.component';
import { LectureService } from '../../../../../../main/webapp/app/entities/lecture/lecture.service';
import { Lecture } from '../../../../../../main/webapp/app/entities/lecture/lecture.model';

describe('Component Tests', () => {

    describe('Lecture Management Detail Component', () => {
        let comp: LectureDetailComponent;
        let fixture: ComponentFixture<LectureDetailComponent>;
        let service: LectureService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [KampusTestModule],
                declarations: [LectureDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    LectureService,
                    JhiEventManager
                ]
            }).overrideTemplate(LectureDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(LectureDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(LectureService);
        });


        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Lecture(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.lecture).toEqual(jasmine.objectContaining({id:10}));
            });
        });
    });

});
