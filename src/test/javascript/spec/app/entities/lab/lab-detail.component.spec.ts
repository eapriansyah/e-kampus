import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { KampusTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { LabDetailComponent } from '../../../../../../main/webapp/app/entities/lab/lab-detail.component';
import { LabService } from '../../../../../../main/webapp/app/entities/lab/lab.service';
import { Lab } from '../../../../../../main/webapp/app/entities/lab/lab.model';

describe('Component Tests', () => {

    describe('Lab Management Detail Component', () => {
        let comp: LabDetailComponent;
        let fixture: ComponentFixture<LabDetailComponent>;
        let service: LabService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [KampusTestModule],
                declarations: [LabDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    LabService,
                    JhiEventManager
                ]
            }).overrideTemplate(LabDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(LabDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(LabService);
        });


        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Lab(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.lab).toEqual(jasmine.objectContaining({id:10}));
            });
        });
    });

});
