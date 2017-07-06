import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager  } from 'ng-jhipster';

import {Internal} from './internal.model';
import {InternalService} from './internal.service';

@Component({
    selector: 'jhi-internal-detail',
    templateUrl: './internal-detail.component.html'
})
export class InternalDetailComponent implements OnInit, OnDestroy {

    internal: Internal;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private internalService: InternalService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInInternals();
    }

    load(id) {
        this.internalService.find(id).subscribe((internal) => {
            this.internal = internal;
        });
    }

    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInInternals() {
        this.eventSubscriber = this.eventManager.subscribe(
            'internalListModification',
            (response) => this.load(this.internal.idPartyRole)
        );
    }
}
