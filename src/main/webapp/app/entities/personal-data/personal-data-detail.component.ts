import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager  } from 'ng-jhipster';

import {PersonalData} from './personal-data.model';
import {PersonalDataService} from './personal-data.service';

@Component({
    selector: 'jhi-personal-data-detail',
    templateUrl: './personal-data-detail.component.html'
})
export class PersonalDataDetailComponent implements OnInit, OnDestroy {

    personalData: PersonalData;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private personalDataService: PersonalDataService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInPersonalData();
    }

    load(id) {
        this.personalDataService.find(id).subscribe((personalData) => {
            this.personalData = personalData;
        });
    }

    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInPersonalData() {
        this.eventSubscriber = this.eventManager.subscribe(
            'personalDataListModification',
            (response) => this.load(this.personalData.idPersonalData)
        );
    }
}
