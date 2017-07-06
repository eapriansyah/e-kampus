import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager  } from 'ng-jhipster';

import {EventAction} from './event-action.model';
import {EventActionService} from './event-action.service';

@Component({
    selector: 'jhi-event-action-detail',
    templateUrl: './event-action-detail.component.html'
})
export class EventActionDetailComponent implements OnInit, OnDestroy {

    eventAction: EventAction;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private eventActionService: EventActionService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInEventActions();
    }

    load(id) {
        this.eventActionService.find(id).subscribe((eventAction) => {
            this.eventAction = eventAction;
        });
    }

    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInEventActions() {
        this.eventSubscriber = this.eventManager.subscribe(
            'eventActionListModification',
            (response) => this.load(this.eventAction.idEventAction)
        );
    }
}
