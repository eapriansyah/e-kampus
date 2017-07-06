import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { KampusTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { TelecomunicationNumberDetailComponent } from '../../../../../../main/webapp/app/entities/telecomunication-number/telecomunication-number-detail.component';
import { TelecomunicationNumberService } from '../../../../../../main/webapp/app/entities/telecomunication-number/telecomunication-number.service';
import { TelecomunicationNumber } from '../../../../../../main/webapp/app/entities/telecomunication-number/telecomunication-number.model';

describe('Component Tests', () => {

    describe('TelecomunicationNumber Management Detail Component', () => {
        let comp: TelecomunicationNumberDetailComponent;
        let fixture: ComponentFixture<TelecomunicationNumberDetailComponent>;
        let service: TelecomunicationNumberService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [KampusTestModule],
                declarations: [TelecomunicationNumberDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    TelecomunicationNumberService,
                    JhiEventManager
                ]
            }).overrideTemplate(TelecomunicationNumberDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(TelecomunicationNumberDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(TelecomunicationNumberService);
        });


        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new TelecomunicationNumber(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.telecomunicationNumber).toEqual(jasmine.objectContaining({id:10}));
            });
        });
    });

});
