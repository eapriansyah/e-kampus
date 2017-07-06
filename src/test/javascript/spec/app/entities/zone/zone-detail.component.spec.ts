import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { KampusTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { ZoneDetailComponent } from '../../../../../../main/webapp/app/entities/zone/zone-detail.component';
import { ZoneService } from '../../../../../../main/webapp/app/entities/zone/zone.service';
import { Zone } from '../../../../../../main/webapp/app/entities/zone/zone.model';

describe('Component Tests', () => {

    describe('Zone Management Detail Component', () => {
        let comp: ZoneDetailComponent;
        let fixture: ComponentFixture<ZoneDetailComponent>;
        let service: ZoneService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [KampusTestModule],
                declarations: [ZoneDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    ZoneService,
                    JhiEventManager
                ]
            }).overrideTemplate(ZoneDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ZoneDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ZoneService);
        });


        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Zone(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.zone).toEqual(jasmine.objectContaining({id:10}));
            });
        });
    });

});
