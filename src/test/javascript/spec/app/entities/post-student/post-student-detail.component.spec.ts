import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { KampusTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { PostStudentDetailComponent } from '../../../../../../main/webapp/app/entities/post-student/post-student-detail.component';
import { PostStudentService } from '../../../../../../main/webapp/app/entities/post-student/post-student.service';
import { PostStudent } from '../../../../../../main/webapp/app/entities/post-student/post-student.model';

describe('Component Tests', () => {

    describe('PostStudent Management Detail Component', () => {
        let comp: PostStudentDetailComponent;
        let fixture: ComponentFixture<PostStudentDetailComponent>;
        let service: PostStudentService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [KampusTestModule],
                declarations: [PostStudentDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    PostStudentService,
                    JhiEventManager
                ]
            }).overrideTemplate(PostStudentDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(PostStudentDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(PostStudentService);
        });


        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new PostStudent(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.postStudent).toEqual(jasmine.objectContaining({id:10}));
            });
        });
    });

});
