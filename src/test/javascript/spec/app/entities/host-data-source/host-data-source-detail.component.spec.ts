import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { KampusTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { HostDataSourceDetailComponent } from '../../../../../../main/webapp/app/entities/host-data-source/host-data-source-detail.component';
import { HostDataSourceService } from '../../../../../../main/webapp/app/entities/host-data-source/host-data-source.service';
import { HostDataSource } from '../../../../../../main/webapp/app/entities/host-data-source/host-data-source.model';

describe('Component Tests', () => {

    describe('HostDataSource Management Detail Component', () => {
        let comp: HostDataSourceDetailComponent;
        let fixture: ComponentFixture<HostDataSourceDetailComponent>;
        let service: HostDataSourceService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [KampusTestModule],
                declarations: [HostDataSourceDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    HostDataSourceService,
                    JhiEventManager
                ]
            }).overrideTemplate(HostDataSourceDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(HostDataSourceDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(HostDataSourceService);
        });


        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new HostDataSource(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.hostDataSource).toEqual(jasmine.objectContaining({id:10}));
            });
        });
    });

});
