import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { KampusTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { ReligionTypeDetailComponent } from '../../../../../../main/webapp/app/entities/religion-type/religion-type-detail.component';
import { ReligionTypeService } from '../../../../../../main/webapp/app/entities/religion-type/religion-type.service';
import { ReligionType } from '../../../../../../main/webapp/app/entities/religion-type/religion-type.model';

describe('Component Tests', () => {

    describe('ReligionType Management Detail Component', () => {
        let comp: ReligionTypeDetailComponent;
        let fixture: ComponentFixture<ReligionTypeDetailComponent>;
        let service: ReligionTypeService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [KampusTestModule],
                declarations: [ReligionTypeDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    ReligionTypeService,
                    JhiEventManager
                ]
            }).overrideTemplate(ReligionTypeDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ReligionTypeDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ReligionTypeService);
        });


        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new ReligionType(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.religionType).toEqual(jasmine.objectContaining({id:10}));
            });
        });
    });

});
