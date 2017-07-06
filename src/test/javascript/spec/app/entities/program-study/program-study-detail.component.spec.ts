import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { KampusTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { ProgramStudyDetailComponent } from '../../../../../../main/webapp/app/entities/program-study/program-study-detail.component';
import { ProgramStudyService } from '../../../../../../main/webapp/app/entities/program-study/program-study.service';
import { ProgramStudy } from '../../../../../../main/webapp/app/entities/program-study/program-study.model';

describe('Component Tests', () => {

    describe('ProgramStudy Management Detail Component', () => {
        let comp: ProgramStudyDetailComponent;
        let fixture: ComponentFixture<ProgramStudyDetailComponent>;
        let service: ProgramStudyService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [KampusTestModule],
                declarations: [ProgramStudyDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    ProgramStudyService,
                    JhiEventManager
                ]
            }).overrideTemplate(ProgramStudyDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ProgramStudyDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ProgramStudyService);
        });


        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new ProgramStudy(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.programStudy).toEqual(jasmine.objectContaining({id:10}));
            });
        });
    });

});
