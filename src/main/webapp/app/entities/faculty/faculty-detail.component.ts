import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager  } from 'ng-jhipster';

import {Faculty} from './faculty.model';
import {FacultyService} from './faculty.service';

@Component({
    selector: 'jhi-faculty-detail',
    templateUrl: './faculty-detail.component.html'
})
export class FacultyDetailComponent implements OnInit, OnDestroy {

    faculty: Faculty;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private facultyService: FacultyService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInFaculties();
    }

    load(id) {
        this.facultyService.find(id).subscribe((faculty) => {
            this.faculty = faculty;
        });
    }

    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInFaculties() {
        this.eventSubscriber = this.eventManager.subscribe(
            'facultyListModification',
            (response) => this.load(this.faculty.idPartyRole)
        );
    }
}
