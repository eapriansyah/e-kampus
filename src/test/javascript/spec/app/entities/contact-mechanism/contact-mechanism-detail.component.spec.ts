import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { KampusTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { ContactMechanismDetailComponent } from '../../../../../../main/webapp/app/entities/contact-mechanism/contact-mechanism-detail.component';
import { ContactMechanismService } from '../../../../../../main/webapp/app/entities/contact-mechanism/contact-mechanism.service';
import { ContactMechanism } from '../../../../../../main/webapp/app/entities/contact-mechanism/contact-mechanism.model';

describe('Component Tests', () => {

    describe('ContactMechanism Management Detail Component', () => {
        let comp: ContactMechanismDetailComponent;
        let fixture: ComponentFixture<ContactMechanismDetailComponent>;
        let service: ContactMechanismService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [KampusTestModule],
                declarations: [ContactMechanismDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    ContactMechanismService,
                    JhiEventManager
                ]
            }).overrideTemplate(ContactMechanismDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ContactMechanismDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ContactMechanismService);
        });


        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new ContactMechanism(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.contactMechanism).toEqual(jasmine.objectContaining({id:10}));
            });
        });
    });

});
