import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { KampusTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { StudentEventDetailComponent } from '../../../../../../main/webapp/app/entities/student-event/student-event-detail.component';
import { StudentEventService } from '../../../../../../main/webapp/app/entities/student-event/student-event.service';
import { StudentEvent } from '../../../../../../main/webapp/app/entities/student-event/student-event.model';

describe('Component Tests', () => {

    describe('StudentEvent Management Detail Component', () => {
        let comp: StudentEventDetailComponent;
        let fixture: ComponentFixture<StudentEventDetailComponent>;
        let service: StudentEventService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [KampusTestModule],
                declarations: [StudentEventDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    StudentEventService,
                    JhiEventManager
                ]
            }).overrideTemplate(StudentEventDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(StudentEventDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(StudentEventService);
        });


        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new StudentEvent(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.studentEvent).toEqual(jasmine.objectContaining({id:10}));
            });
        });
    });

});
