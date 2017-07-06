import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { KampusTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { StudentPeriodDataDetailComponent } from '../../../../../../main/webapp/app/entities/student-period-data/student-period-data-detail.component';
import { StudentPeriodDataService } from '../../../../../../main/webapp/app/entities/student-period-data/student-period-data.service';
import { StudentPeriodData } from '../../../../../../main/webapp/app/entities/student-period-data/student-period-data.model';

describe('Component Tests', () => {

    describe('StudentPeriodData Management Detail Component', () => {
        let comp: StudentPeriodDataDetailComponent;
        let fixture: ComponentFixture<StudentPeriodDataDetailComponent>;
        let service: StudentPeriodDataService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [KampusTestModule],
                declarations: [StudentPeriodDataDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    StudentPeriodDataService,
                    JhiEventManager
                ]
            }).overrideTemplate(StudentPeriodDataDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(StudentPeriodDataDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(StudentPeriodDataService);
        });


        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new StudentPeriodData(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.studentPeriodData).toEqual(jasmine.objectContaining({id:10}));
            });
        });
    });

});
