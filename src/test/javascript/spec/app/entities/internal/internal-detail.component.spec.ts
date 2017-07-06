import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { KampusTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { InternalDetailComponent } from '../../../../../../main/webapp/app/entities/internal/internal-detail.component';
import { InternalService } from '../../../../../../main/webapp/app/entities/internal/internal.service';
import { Internal } from '../../../../../../main/webapp/app/entities/internal/internal.model';

describe('Component Tests', () => {

    describe('Internal Management Detail Component', () => {
        let comp: InternalDetailComponent;
        let fixture: ComponentFixture<InternalDetailComponent>;
        let service: InternalService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [KampusTestModule],
                declarations: [InternalDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    InternalService,
                    JhiEventManager
                ]
            }).overrideTemplate(InternalDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(InternalDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(InternalService);
        });


        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Internal(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.internal).toEqual(jasmine.objectContaining({id:10}));
            });
        });
    });

});
