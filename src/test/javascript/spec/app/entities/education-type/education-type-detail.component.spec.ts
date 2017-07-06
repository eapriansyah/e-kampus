import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { KampusTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { EducationTypeDetailComponent } from '../../../../../../main/webapp/app/entities/education-type/education-type-detail.component';
import { EducationTypeService } from '../../../../../../main/webapp/app/entities/education-type/education-type.service';
import { EducationType } from '../../../../../../main/webapp/app/entities/education-type/education-type.model';

describe('Component Tests', () => {

    describe('EducationType Management Detail Component', () => {
        let comp: EducationTypeDetailComponent;
        let fixture: ComponentFixture<EducationTypeDetailComponent>;
        let service: EducationTypeService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [KampusTestModule],
                declarations: [EducationTypeDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    EducationTypeService,
                    JhiEventManager
                ]
            }).overrideTemplate(EducationTypeDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(EducationTypeDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(EducationTypeService);
        });


        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new EducationType(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.educationType).toEqual(jasmine.objectContaining({id:10}));
            });
        });
    });

});
