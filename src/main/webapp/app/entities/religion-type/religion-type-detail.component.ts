import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager  } from 'ng-jhipster';

import {ReligionType} from './religion-type.model';
import {ReligionTypeService} from './religion-type.service';

@Component({
    selector: 'jhi-religion-type-detail',
    templateUrl: './religion-type-detail.component.html'
})
export class ReligionTypeDetailComponent implements OnInit, OnDestroy {

    religionType: ReligionType;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private religionTypeService: ReligionTypeService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInReligionTypes();
    }

    load(id) {
        this.religionTypeService.find(id).subscribe((religionType) => {
            this.religionType = religionType;
        });
    }

    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInReligionTypes() {
        this.eventSubscriber = this.eventManager.subscribe(
            'religionTypeListModification',
            (response) => this.load(this.religionType.idReligionType)
        );
    }
}
