import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { CourseApplicableComponent } from './course-applicable.component';
import { CourseApplicableDetailComponent } from './course-applicable-detail.component';
import { CourseApplicablePopupComponent } from './course-applicable-dialog.component';
import { CourseApplicableDeletePopupComponent } from './course-applicable-delete-dialog.component';

import { Principal } from '../../shared';

@Injectable()
export class CourseApplicableResolvePagingParams implements Resolve<any> {

    constructor(private paginationUtil: JhiPaginationUtil) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const page = route.queryParams['page'] ? route.queryParams['page'] : '1';
        const sort = route.queryParams['sort'] ? route.queryParams['sort'] : 'idApplCourse,asc';
        return {
            page: this.paginationUtil.parsePage(page),
            predicate: this.paginationUtil.parsePredicate(sort),
            ascending: this.paginationUtil.parseAscending(sort)
      };
    }
}

export const courseApplicableRoute: Routes = [
    {
        path: 'course-applicable',
        component: CourseApplicableComponent,
        resolve: {
            'pagingParams': CourseApplicableResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.courseApplicable.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'course-applicable/:id',
        component: CourseApplicableDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.courseApplicable.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const courseApplicablePopupRoute: Routes = [
    {
        path: 'course-applicable-new',
        component: CourseApplicablePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.courseApplicable.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'course-applicable/:id/edit',
        component: CourseApplicablePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.courseApplicable.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'course-applicable/:id/delete',
        component: CourseApplicableDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.courseApplicable.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
