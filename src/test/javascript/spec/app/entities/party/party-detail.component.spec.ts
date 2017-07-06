import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { KampusTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { PartyDetailComponent } from '../../../../../../main/webapp/app/entities/party/party-detail.component';
import { PartyService } from '../../../../../../main/webapp/app/entities/party/party.service';
import { Party } from '../../../../../../main/webapp/app/entities/party/party.model';

describe('Component Tests', () => {

    describe('Party Management Detail Component', () => {
        let comp: PartyDetailComponent;
        let fixture: ComponentFixture<PartyDetailComponent>;
        let service: PartyService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [KampusTestModule],
                declarations: [PartyDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    PartyService,
                    JhiEventManager
                ]
            }).overrideTemplate(PartyDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(PartyDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(PartyService);
        });


        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Party(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.party).toEqual(jasmine.objectContaining({id:10}));
            });
        });
    });

});
