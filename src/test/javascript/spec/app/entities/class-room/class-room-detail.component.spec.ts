import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { KampusTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { ClassRoomDetailComponent } from '../../../../../../main/webapp/app/entities/class-room/class-room-detail.component';
import { ClassRoomService } from '../../../../../../main/webapp/app/entities/class-room/class-room.service';
import { ClassRoom } from '../../../../../../main/webapp/app/entities/class-room/class-room.model';

describe('Component Tests', () => {

    describe('ClassRoom Management Detail Component', () => {
        let comp: ClassRoomDetailComponent;
        let fixture: ComponentFixture<ClassRoomDetailComponent>;
        let service: ClassRoomService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [KampusTestModule],
                declarations: [ClassRoomDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    ClassRoomService,
                    JhiEventManager
                ]
            }).overrideTemplate(ClassRoomDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ClassRoomDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ClassRoomService);
        });


        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new ClassRoom(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.classRoom).toEqual(jasmine.objectContaining({id:10}));
            });
        });
    });

});
