import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { KampusTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { PersonalDataDetailComponent } from '../../../../../../main/webapp/app/entities/personal-data/personal-data-detail.component';
import { PersonalDataService } from '../../../../../../main/webapp/app/entities/personal-data/personal-data.service';
import { PersonalData } from '../../../../../../main/webapp/app/entities/personal-data/personal-data.model';

describe('Component Tests', () => {

    describe('PersonalData Management Detail Component', () => {
        let comp: PersonalDataDetailComponent;
        let fixture: ComponentFixture<PersonalDataDetailComponent>;
        let service: PersonalDataService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [KampusTestModule],
                declarations: [PersonalDataDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    PersonalDataService,
                    JhiEventManager
                ]
            }).overrideTemplate(PersonalDataDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(PersonalDataDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(PersonalDataService);
        });


        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new PersonalData(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.personalData).toEqual(jasmine.objectContaining({id:10}));
            });
        });
    });

});
