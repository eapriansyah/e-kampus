import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager  } from 'ng-jhipster';

import {PostStudent} from './post-student.model';
import {PostStudentService} from './post-student.service';

@Component({
    selector: 'jhi-post-student-detail',
    templateUrl: './post-student-detail.component.html'
})
export class PostStudentDetailComponent implements OnInit, OnDestroy {

    postStudent: PostStudent;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private postStudentService: PostStudentService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInPostStudents();
    }

    load(id) {
        this.postStudentService.find(id).subscribe((postStudent) => {
            this.postStudent = postStudent;
        });
    }

    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInPostStudents() {
        this.eventSubscriber = this.eventManager.subscribe(
            'postStudentListModification',
            (response) => this.load(this.postStudent.idPartyRole)
        );
    }
}
