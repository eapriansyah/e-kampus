import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { StudyPathComponent } from './study-path.component';
import { StudyPathDetailComponent } from './study-path-detail.component';
import { StudyPathPopupComponent } from './study-path-dialog.component';
import { StudyPathDeletePopupComponent } from './study-path-delete-dialog.component';

import { Principal } from '../../shared';

@Injectable()
export class StudyPathResolvePagingParams implements Resolve<any> {

    constructor(private paginationUtil: JhiPaginationUtil) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const page = route.queryParams['page'] ? route.queryParams['page'] : '1';
        const sort = route.queryParams['sort'] ? route.queryParams['sort'] : 'idStudyPath,asc';
        return {
            page: this.paginationUtil.parsePage(page),
            predicate: this.paginationUtil.parsePredicate(sort),
            ascending: this.paginationUtil.parseAscending(sort)
      };
    }
}

export const studyPathRoute: Routes = [
    {
        path: 'study-path',
        component: StudyPathComponent,
        resolve: {
            'pagingParams': StudyPathResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.studyPath.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'study-path/:id',
        component: StudyPathDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.studyPath.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const studyPathPopupRoute: Routes = [
    {
        path: 'study-path-new',
        component: StudyPathPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.studyPath.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'study-path/:id/edit',
        component: StudyPathPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.studyPath.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'study-path/:id/delete',
        component: StudyPathDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.studyPath.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
