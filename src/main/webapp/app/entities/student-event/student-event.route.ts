import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { StudentEventComponent } from './student-event.component';
import { StudentEventDetailComponent } from './student-event-detail.component';
import { StudentEventPopupComponent } from './student-event-dialog.component';
import { StudentEventDeletePopupComponent } from './student-event-delete-dialog.component';

import { Principal } from '../../shared';

@Injectable()
export class StudentEventResolvePagingParams implements Resolve<any> {

    constructor(private paginationUtil: JhiPaginationUtil) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const page = route.queryParams['page'] ? route.queryParams['page'] : '1';
        const sort = route.queryParams['sort'] ? route.queryParams['sort'] : 'idStudentEvent,asc';
        return {
            page: this.paginationUtil.parsePage(page),
            predicate: this.paginationUtil.parsePredicate(sort),
            ascending: this.paginationUtil.parseAscending(sort)
      };
    }
}

export const studentEventRoute: Routes = [
    {
        path: 'student-event',
        component: StudentEventComponent,
        resolve: {
            'pagingParams': StudentEventResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.studentEvent.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'student-event/:id',
        component: StudentEventDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.studentEvent.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const studentEventPopupRoute: Routes = [
    {
        path: 'student-event-new',
        component: StudentEventPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.studentEvent.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'student-event/:id/edit',
        component: StudentEventPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.studentEvent.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'student-event/:id/delete',
        component: StudentEventDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.studentEvent.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
