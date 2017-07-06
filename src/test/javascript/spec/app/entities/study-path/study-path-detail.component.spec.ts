import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { KampusTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { StudyPathDetailComponent } from '../../../../../../main/webapp/app/entities/study-path/study-path-detail.component';
import { StudyPathService } from '../../../../../../main/webapp/app/entities/study-path/study-path.service';
import { StudyPath } from '../../../../../../main/webapp/app/entities/study-path/study-path.model';

describe('Component Tests', () => {

    describe('StudyPath Management Detail Component', () => {
        let comp: StudyPathDetailComponent;
        let fixture: ComponentFixture<StudyPathDetailComponent>;
        let service: StudyPathService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [KampusTestModule],
                declarations: [StudyPathDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    StudyPathService,
                    JhiEventManager
                ]
            }).overrideTemplate(StudyPathDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(StudyPathDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(StudyPathService);
        });


        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new StudyPath(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.studyPath).toEqual(jasmine.objectContaining({id:10}));
            });
        });
    });

});
