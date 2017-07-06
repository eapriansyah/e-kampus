import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager  } from 'ng-jhipster';

import {Lab} from './lab.model';
import {LabService} from './lab.service';

@Component({
    selector: 'jhi-lab-detail',
    templateUrl: './lab-detail.component.html'
})
export class LabDetailComponent implements OnInit, OnDestroy {

    lab: Lab;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private labService: LabService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInLabs();
    }

    load(id) {
        this.labService.find(id).subscribe((lab) => {
            this.lab = lab;
        });
    }

    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInLabs() {
        this.eventSubscriber = this.eventManager.subscribe(
            'labListModification',
            (response) => this.load(this.lab.idFacility)
        );
    }
}
