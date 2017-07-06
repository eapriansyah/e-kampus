import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { LectureComponent } from './lecture.component';
import { LectureDetailComponent } from './lecture-detail.component';
import { LecturePopupComponent } from './lecture-dialog.component';
import { LectureDeletePopupComponent } from './lecture-delete-dialog.component';

import { Principal } from '../../shared';

@Injectable()
export class LectureResolvePagingParams implements Resolve<any> {

    constructor(private paginationUtil: JhiPaginationUtil) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const page = route.queryParams['page'] ? route.queryParams['page'] : '1';
        const sort = route.queryParams['sort'] ? route.queryParams['sort'] : 'idPartyRole,asc';
        return {
            page: this.paginationUtil.parsePage(page),
            predicate: this.paginationUtil.parsePredicate(sort),
            ascending: this.paginationUtil.parseAscending(sort)
      };
    }
}

export const lectureRoute: Routes = [
    {
        path: 'lecture',
        component: LectureComponent,
        resolve: {
            'pagingParams': LectureResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.lecture.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'lecture/:id',
        component: LectureDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.lecture.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const lecturePopupRoute: Routes = [
    {
        path: 'lecture-new',
        component: LecturePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.lecture.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'lecture/:id/edit',
        component: LecturePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.lecture.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'lecture/:id/delete',
        component: LectureDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.lecture.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
