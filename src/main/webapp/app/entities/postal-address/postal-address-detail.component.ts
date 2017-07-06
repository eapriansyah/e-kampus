import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager  } from 'ng-jhipster';

import {PostalAddress} from './postal-address.model';
import {PostalAddressService} from './postal-address.service';

@Component({
    selector: 'jhi-postal-address-detail',
    templateUrl: './postal-address-detail.component.html'
})
export class PostalAddressDetailComponent implements OnInit, OnDestroy {

    postalAddress: PostalAddress;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private postalAddressService: PostalAddressService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInPostalAddresses();
    }

    load(id) {
        this.postalAddressService.find(id).subscribe((postalAddress) => {
            this.postalAddress = postalAddress;
        });
    }

    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInPostalAddresses() {
        this.eventSubscriber = this.eventManager.subscribe(
            'postalAddressListModification',
            (response) => this.load(this.postalAddress.idContact)
        );
    }
}
