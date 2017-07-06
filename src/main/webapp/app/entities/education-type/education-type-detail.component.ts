import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager  } from 'ng-jhipster';

import {EducationType} from './education-type.model';
import {EducationTypeService} from './education-type.service';

@Component({
    selector: 'jhi-education-type-detail',
    templateUrl: './education-type-detail.component.html'
})
export class EducationTypeDetailComponent implements OnInit, OnDestroy {

    educationType: EducationType;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private educationTypeService: EducationTypeService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInEducationTypes();
    }

    load(id) {
        this.educationTypeService.find(id).subscribe((educationType) => {
            this.educationType = educationType;
        });
    }

    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInEducationTypes() {
        this.eventSubscriber = this.eventManager.subscribe(
            'educationTypeListModification',
            (response) => this.load(this.educationType.idEducationType)
        );
    }
}
