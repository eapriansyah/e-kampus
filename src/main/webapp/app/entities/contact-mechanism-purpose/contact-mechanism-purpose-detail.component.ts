import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager  } from 'ng-jhipster';

import {ContactMechanismPurpose} from './contact-mechanism-purpose.model';
import {ContactMechanismPurposeService} from './contact-mechanism-purpose.service';

@Component({
    selector: 'jhi-contact-mechanism-purpose-detail',
    templateUrl: './contact-mechanism-purpose-detail.component.html'
})
export class ContactMechanismPurposeDetailComponent implements OnInit, OnDestroy {

    contactMechanismPurpose: ContactMechanismPurpose;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private contactMechanismPurposeService: ContactMechanismPurposeService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInContactMechanismPurposes();
    }

    load(id) {
        this.contactMechanismPurposeService.find(id).subscribe((contactMechanismPurpose) => {
            this.contactMechanismPurpose = contactMechanismPurpose;
        });
    }

    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInContactMechanismPurposes() {
        this.eventSubscriber = this.eventManager.subscribe(
            'contactMechanismPurposeListModification',
            (response) => this.load(this.contactMechanismPurpose.idContactMechPurpose)
        );
    }
}
