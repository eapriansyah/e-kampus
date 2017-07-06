import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager  } from 'ng-jhipster';

import {HostDataSource} from './host-data-source.model';
import {HostDataSourceService} from './host-data-source.service';

@Component({
    selector: 'jhi-host-data-source-detail',
    templateUrl: './host-data-source-detail.component.html'
})
export class HostDataSourceDetailComponent implements OnInit, OnDestroy {

    hostDataSource: HostDataSource;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private hostDataSourceService: HostDataSourceService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInHostDataSources();
    }

    load(id) {
        this.hostDataSourceService.find(id).subscribe((hostDataSource) => {
            this.hostDataSource = hostDataSource;
        });
    }

    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInHostDataSources() {
        this.eventSubscriber = this.eventManager.subscribe(
            'hostDataSourceListModification',
            (response) => this.load(this.hostDataSource.idHostDataSource)
        );
    }
}
