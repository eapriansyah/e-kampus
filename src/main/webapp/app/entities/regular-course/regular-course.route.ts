import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { RegularCourseComponent } from './regular-course.component';
import { RegularCourseDetailComponent } from './regular-course-detail.component';
import { RegularCoursePopupComponent } from './regular-course-dialog.component';
import { RegularCourseDeletePopupComponent } from './regular-course-delete-dialog.component';

import { Principal } from '../../shared';

@Injectable()
export class RegularCourseResolvePagingParams implements Resolve<any> {

    constructor(private paginationUtil: JhiPaginationUtil) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const page = route.queryParams['page'] ? route.queryParams['page'] : '1';
        const sort = route.queryParams['sort'] ? route.queryParams['sort'] : 'idCourse,asc';
        return {
            page: this.paginationUtil.parsePage(page),
            predicate: this.paginationUtil.parsePredicate(sort),
            ascending: this.paginationUtil.parseAscending(sort)
      };
    }
}

export const regularCourseRoute: Routes = [
    {
        path: 'regular-course',
        component: RegularCourseComponent,
        resolve: {
            'pagingParams': RegularCourseResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.regularCourse.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'regular-course/:id',
        component: RegularCourseDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.regularCourse.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const regularCoursePopupRoute: Routes = [
    {
        path: 'regular-course-new',
        component: RegularCoursePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.regularCourse.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'regular-course/:id/edit',
        component: RegularCoursePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.regularCourse.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'regular-course/:id/delete',
        component: RegularCourseDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.regularCourse.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
