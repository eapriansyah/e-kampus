import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { KampusTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { ElectronicAddressDetailComponent } from '../../../../../../main/webapp/app/entities/electronic-address/electronic-address-detail.component';
import { ElectronicAddressService } from '../../../../../../main/webapp/app/entities/electronic-address/electronic-address.service';
import { ElectronicAddress } from '../../../../../../main/webapp/app/entities/electronic-address/electronic-address.model';

describe('Component Tests', () => {

    describe('ElectronicAddress Management Detail Component', () => {
        let comp: ElectronicAddressDetailComponent;
        let fixture: ComponentFixture<ElectronicAddressDetailComponent>;
        let service: ElectronicAddressService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [KampusTestModule],
                declarations: [ElectronicAddressDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    ElectronicAddressService,
                    JhiEventManager
                ]
            }).overrideTemplate(ElectronicAddressDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ElectronicAddressDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ElectronicAddressService);
        });


        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new ElectronicAddress(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.electronicAddress).toEqual(jasmine.objectContaining({id:10}));
            });
        });
    });

});
