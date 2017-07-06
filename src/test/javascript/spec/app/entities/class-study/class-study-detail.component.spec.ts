import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { KampusTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { ClassStudyDetailComponent } from '../../../../../../main/webapp/app/entities/class-study/class-study-detail.component';
import { ClassStudyService } from '../../../../../../main/webapp/app/entities/class-study/class-study.service';
import { ClassStudy } from '../../../../../../main/webapp/app/entities/class-study/class-study.model';

describe('Component Tests', () => {

    describe('ClassStudy Management Detail Component', () => {
        let comp: ClassStudyDetailComponent;
        let fixture: ComponentFixture<ClassStudyDetailComponent>;
        let service: ClassStudyService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [KampusTestModule],
                declarations: [ClassStudyDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    ClassStudyService,
                    JhiEventManager
                ]
            }).overrideTemplate(ClassStudyDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ClassStudyDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ClassStudyService);
        });


        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new ClassStudy(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.classStudy).toEqual(jasmine.objectContaining({id:10}));
            });
        });
    });

});
