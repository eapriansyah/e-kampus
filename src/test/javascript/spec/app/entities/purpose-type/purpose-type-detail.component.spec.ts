import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { KampusTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { PurposeTypeDetailComponent } from '../../../../../../main/webapp/app/entities/purpose-type/purpose-type-detail.component';
import { PurposeTypeService } from '../../../../../../main/webapp/app/entities/purpose-type/purpose-type.service';
import { PurposeType } from '../../../../../../main/webapp/app/entities/purpose-type/purpose-type.model';

describe('Component Tests', () => {

    describe('PurposeType Management Detail Component', () => {
        let comp: PurposeTypeDetailComponent;
        let fixture: ComponentFixture<PurposeTypeDetailComponent>;
        let service: PurposeTypeService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [KampusTestModule],
                declarations: [PurposeTypeDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    PurposeTypeService,
                    JhiEventManager
                ]
            }).overrideTemplate(PurposeTypeDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(PurposeTypeDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(PurposeTypeService);
        });


        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new PurposeType(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.purposeType).toEqual(jasmine.objectContaining({id:10}));
            });
        });
    });

});
