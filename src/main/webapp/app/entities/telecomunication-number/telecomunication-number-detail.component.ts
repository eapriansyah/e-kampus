import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager  } from 'ng-jhipster';

import {TelecomunicationNumber} from './telecomunication-number.model';
import {TelecomunicationNumberService} from './telecomunication-number.service';

@Component({
    selector: 'jhi-telecomunication-number-detail',
    templateUrl: './telecomunication-number-detail.component.html'
})
export class TelecomunicationNumberDetailComponent implements OnInit, OnDestroy {

    telecomunicationNumber: TelecomunicationNumber;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private telecomunicationNumberService: TelecomunicationNumberService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInTelecomunicationNumbers();
    }

    load(id) {
        this.telecomunicationNumberService.find(id).subscribe((telecomunicationNumber) => {
            this.telecomunicationNumber = telecomunicationNumber;
        });
    }

    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInTelecomunicationNumbers() {
        this.eventSubscriber = this.eventManager.subscribe(
            'telecomunicationNumberListModification',
            (response) => this.load(this.telecomunicationNumber.idContact)
        );
    }
}
