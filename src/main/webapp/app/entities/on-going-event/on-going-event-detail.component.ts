import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager  } from 'ng-jhipster';

import {OnGoingEvent} from './on-going-event.model';
import {OnGoingEventService} from './on-going-event.service';

@Component({
    selector: 'jhi-on-going-event-detail',
    templateUrl: './on-going-event-detail.component.html'
})
export class OnGoingEventDetailComponent implements OnInit, OnDestroy {

    onGoingEvent: OnGoingEvent;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private onGoingEventService: OnGoingEventService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInOnGoingEvents();
    }

    load(id) {
        this.onGoingEventService.find(id).subscribe((onGoingEvent) => {
            this.onGoingEvent = onGoingEvent;
        });
    }

    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInOnGoingEvents() {
        this.eventSubscriber = this.eventManager.subscribe(
            'onGoingEventListModification',
            (response) => this.load(this.onGoingEvent.idEventGo)
        );
    }
}
