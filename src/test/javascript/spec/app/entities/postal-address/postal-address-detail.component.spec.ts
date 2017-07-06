import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { KampusTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { PostalAddressDetailComponent } from '../../../../../../main/webapp/app/entities/postal-address/postal-address-detail.component';
import { PostalAddressService } from '../../../../../../main/webapp/app/entities/postal-address/postal-address.service';
import { PostalAddress } from '../../../../../../main/webapp/app/entities/postal-address/postal-address.model';

describe('Component Tests', () => {

    describe('PostalAddress Management Detail Component', () => {
        let comp: PostalAddressDetailComponent;
        let fixture: ComponentFixture<PostalAddressDetailComponent>;
        let service: PostalAddressService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [KampusTestModule],
                declarations: [PostalAddressDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    PostalAddressService,
                    JhiEventManager
                ]
            }).overrideTemplate(PostalAddressDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(PostalAddressDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(PostalAddressService);
        });


        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new PostalAddress(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.postalAddress).toEqual(jasmine.objectContaining({id:10}));
            });
        });
    });

});
