import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager  } from 'ng-jhipster';

import {Party} from './party.model';
import {PartyService} from './party.service';

@Component({
    selector: 'jhi-party-detail',
    templateUrl: './party-detail.component.html'
})
export class PartyDetailComponent implements OnInit, OnDestroy {

    party: Party;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private partyService: PartyService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInParties();
    }

    load(id) {
        this.partyService.find(id).subscribe((party) => {
            this.party = party;
        });
    }

    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInParties() {
        this.eventSubscriber = this.eventManager.subscribe(
            'partyListModification',
            (response) => this.load(this.party.idParty)
        );
    }
}
