import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { KampusTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { WorkTypeDetailComponent } from '../../../../../../main/webapp/app/entities/work-type/work-type-detail.component';
import { WorkTypeService } from '../../../../../../main/webapp/app/entities/work-type/work-type.service';
import { WorkType } from '../../../../../../main/webapp/app/entities/work-type/work-type.model';

describe('Component Tests', () => {

    describe('WorkType Management Detail Component', () => {
        let comp: WorkTypeDetailComponent;
        let fixture: ComponentFixture<WorkTypeDetailComponent>;
        let service: WorkTypeService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [KampusTestModule],
                declarations: [WorkTypeDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    WorkTypeService,
                    JhiEventManager
                ]
            }).overrideTemplate(WorkTypeDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(WorkTypeDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(WorkTypeService);
        });


        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new WorkType(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.workType).toEqual(jasmine.objectContaining({id:10}));
            });
        });
    });

});
