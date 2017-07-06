import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { KampusTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { EventActionDetailComponent } from '../../../../../../main/webapp/app/entities/event-action/event-action-detail.component';
import { EventActionService } from '../../../../../../main/webapp/app/entities/event-action/event-action.service';
import { EventAction } from '../../../../../../main/webapp/app/entities/event-action/event-action.model';

describe('Component Tests', () => {

    describe('EventAction Management Detail Component', () => {
        let comp: EventActionDetailComponent;
        let fixture: ComponentFixture<EventActionDetailComponent>;
        let service: EventActionService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [KampusTestModule],
                declarations: [EventActionDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    EventActionService,
                    JhiEventManager
                ]
            }).overrideTemplate(EventActionDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(EventActionDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(EventActionService);
        });


        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new EventAction(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.eventAction).toEqual(jasmine.objectContaining({id:10}));
            });
        });
    });

});
