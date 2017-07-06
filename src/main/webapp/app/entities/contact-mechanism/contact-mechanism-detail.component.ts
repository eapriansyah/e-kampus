import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager  } from 'ng-jhipster';

import {ContactMechanism} from './contact-mechanism.model';
import {ContactMechanismService} from './contact-mechanism.service';

@Component({
    selector: 'jhi-contact-mechanism-detail',
    templateUrl: './contact-mechanism-detail.component.html'
})
export class ContactMechanismDetailComponent implements OnInit, OnDestroy {

    contactMechanism: ContactMechanism;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private contactMechanismService: ContactMechanismService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInContactMechanisms();
    }

    load(id) {
        this.contactMechanismService.find(id).subscribe((contactMechanism) => {
            this.contactMechanism = contactMechanism;
        });
    }

    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInContactMechanisms() {
        this.eventSubscriber = this.eventManager.subscribe(
            'contactMechanismListModification',
            (response) => this.load(this.contactMechanism.idContact)
        );
    }
}
