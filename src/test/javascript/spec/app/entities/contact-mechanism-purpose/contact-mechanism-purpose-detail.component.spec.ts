import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { KampusTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { ContactMechanismPurposeDetailComponent } from '../../../../../../main/webapp/app/entities/contact-mechanism-purpose/contact-mechanism-purpose-detail.component';
import { ContactMechanismPurposeService } from '../../../../../../main/webapp/app/entities/contact-mechanism-purpose/contact-mechanism-purpose.service';
import { ContactMechanismPurpose } from '../../../../../../main/webapp/app/entities/contact-mechanism-purpose/contact-mechanism-purpose.model';

describe('Component Tests', () => {

    describe('ContactMechanismPurpose Management Detail Component', () => {
        let comp: ContactMechanismPurposeDetailComponent;
        let fixture: ComponentFixture<ContactMechanismPurposeDetailComponent>;
        let service: ContactMechanismPurposeService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [KampusTestModule],
                declarations: [ContactMechanismPurposeDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    ContactMechanismPurposeService,
                    JhiEventManager
                ]
            }).overrideTemplate(ContactMechanismPurposeDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ContactMechanismPurposeDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ContactMechanismPurposeService);
        });


        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new ContactMechanismPurpose(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.contactMechanismPurpose).toEqual(jasmine.objectContaining({id:10}));
            });
        });
    });

});
