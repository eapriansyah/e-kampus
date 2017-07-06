import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager  } from 'ng-jhipster';

import {PurposeType} from './purpose-type.model';
import {PurposeTypeService} from './purpose-type.service';

@Component({
    selector: 'jhi-purpose-type-detail',
    templateUrl: './purpose-type-detail.component.html'
})
export class PurposeTypeDetailComponent implements OnInit, OnDestroy {

    purposeType: PurposeType;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private purposeTypeService: PurposeTypeService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInPurposeTypes();
    }

    load(id) {
        this.purposeTypeService.find(id).subscribe((purposeType) => {
            this.purposeType = purposeType;
        });
    }

    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInPurposeTypes() {
        this.eventSubscriber = this.eventManager.subscribe(
            'purposeTypeListModification',
            (response) => this.load(this.purposeType.idPurposeType)
        );
    }
}
