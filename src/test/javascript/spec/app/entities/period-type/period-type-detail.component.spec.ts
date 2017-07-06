import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { KampusTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { PeriodTypeDetailComponent } from '../../../../../../main/webapp/app/entities/period-type/period-type-detail.component';
import { PeriodTypeService } from '../../../../../../main/webapp/app/entities/period-type/period-type.service';
import { PeriodType } from '../../../../../../main/webapp/app/entities/period-type/period-type.model';

describe('Component Tests', () => {

    describe('PeriodType Management Detail Component', () => {
        let comp: PeriodTypeDetailComponent;
        let fixture: ComponentFixture<PeriodTypeDetailComponent>;
        let service: PeriodTypeService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [KampusTestModule],
                declarations: [PeriodTypeDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    PeriodTypeService,
                    JhiEventManager
                ]
            }).overrideTemplate(PeriodTypeDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(PeriodTypeDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(PeriodTypeService);
        });


        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new PeriodType(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.periodType).toEqual(jasmine.objectContaining({id:10}));
            });
        });
    });

});
