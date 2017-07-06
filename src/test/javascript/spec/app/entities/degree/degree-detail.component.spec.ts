import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { KampusTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { DegreeDetailComponent } from '../../../../../../main/webapp/app/entities/degree/degree-detail.component';
import { DegreeService } from '../../../../../../main/webapp/app/entities/degree/degree.service';
import { Degree } from '../../../../../../main/webapp/app/entities/degree/degree.model';

describe('Component Tests', () => {

    describe('Degree Management Detail Component', () => {
        let comp: DegreeDetailComponent;
        let fixture: ComponentFixture<DegreeDetailComponent>;
        let service: DegreeService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [KampusTestModule],
                declarations: [DegreeDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    DegreeService,
                    JhiEventManager
                ]
            }).overrideTemplate(DegreeDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(DegreeDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(DegreeService);
        });


        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Degree(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.degree).toEqual(jasmine.objectContaining({id:10}));
            });
        });
    });

});
