import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager  } from 'ng-jhipster';

import {ElectronicAddress} from './electronic-address.model';
import {ElectronicAddressService} from './electronic-address.service';

@Component({
    selector: 'jhi-electronic-address-detail',
    templateUrl: './electronic-address-detail.component.html'
})
export class ElectronicAddressDetailComponent implements OnInit, OnDestroy {

    electronicAddress: ElectronicAddress;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private electronicAddressService: ElectronicAddressService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInElectronicAddresses();
    }

    load(id) {
        this.electronicAddressService.find(id).subscribe((electronicAddress) => {
            this.electronicAddress = electronicAddress;
        });
    }

    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInElectronicAddresses() {
        this.eventSubscriber = this.eventManager.subscribe(
            'electronicAddressListModification',
            (response) => this.load(this.electronicAddress.idContact)
        );
    }
}
