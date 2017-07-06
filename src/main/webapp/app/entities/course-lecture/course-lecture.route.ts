import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { CourseLectureComponent } from './course-lecture.component';
import { CourseLectureDetailComponent } from './course-lecture-detail.component';
import { CourseLecturePopupComponent } from './course-lecture-dialog.component';
import { CourseLectureDeletePopupComponent } from './course-lecture-delete-dialog.component';

import { Principal } from '../../shared';

@Injectable()
export class CourseLectureResolvePagingParams implements Resolve<any> {

    constructor(private paginationUtil: JhiPaginationUtil) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const page = route.queryParams['page'] ? route.queryParams['page'] : '1';
        const sort = route.queryParams['sort'] ? route.queryParams['sort'] : 'idCourseLecture,asc';
        return {
            page: this.paginationUtil.parsePage(page),
            predicate: this.paginationUtil.parsePredicate(sort),
            ascending: this.paginationUtil.parseAscending(sort)
      };
    }
}

export const courseLectureRoute: Routes = [
    {
        path: 'course-lecture',
        component: CourseLectureComponent,
        resolve: {
            'pagingParams': CourseLectureResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.courseLecture.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'course-lecture/:id',
        component: CourseLectureDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.courseLecture.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const courseLecturePopupRoute: Routes = [
    {
        path: 'course-lecture-new',
        component: CourseLecturePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.courseLecture.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'course-lecture/:id/edit',
        component: CourseLecturePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.courseLecture.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'course-lecture/:id/delete',
        component: CourseLectureDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.courseLecture.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
