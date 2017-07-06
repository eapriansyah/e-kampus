import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { KampusTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { OnGoingEventDetailComponent } from '../../../../../../main/webapp/app/entities/on-going-event/on-going-event-detail.component';
import { OnGoingEventService } from '../../../../../../main/webapp/app/entities/on-going-event/on-going-event.service';
import { OnGoingEvent } from '../../../../../../main/webapp/app/entities/on-going-event/on-going-event.model';

describe('Component Tests', () => {

    describe('OnGoingEvent Management Detail Component', () => {
        let comp: OnGoingEventDetailComponent;
        let fixture: ComponentFixture<OnGoingEventDetailComponent>;
        let service: OnGoingEventService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [KampusTestModule],
                declarations: [OnGoingEventDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    OnGoingEventService,
                    JhiEventManager
                ]
            }).overrideTemplate(OnGoingEventDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(OnGoingEventDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(OnGoingEventService);
        });


        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new OnGoingEvent(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.onGoingEvent).toEqual(jasmine.objectContaining({id:10}));
            });
        });
    });

});
