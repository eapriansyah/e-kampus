import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { KampusTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { PreStudentDetailComponent } from '../../../../../../main/webapp/app/entities/pre-student/pre-student-detail.component';
import { PreStudentService } from '../../../../../../main/webapp/app/entities/pre-student/pre-student.service';
import { PreStudent } from '../../../../../../main/webapp/app/entities/pre-student/pre-student.model';

describe('Component Tests', () => {

    describe('PreStudent Management Detail Component', () => {
        let comp: PreStudentDetailComponent;
        let fixture: ComponentFixture<PreStudentDetailComponent>;
        let service: PreStudentService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [KampusTestModule],
                declarations: [PreStudentDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    PreStudentService,
                    JhiEventManager
                ]
            }).overrideTemplate(PreStudentDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(PreStudentDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(PreStudentService);
        });


        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new PreStudent(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.preStudent).toEqual(jasmine.objectContaining({id:10}));
            });
        });
    });

});
