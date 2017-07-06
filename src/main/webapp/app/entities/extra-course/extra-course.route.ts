import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { ExtraCourseComponent } from './extra-course.component';
import { ExtraCourseDetailComponent } from './extra-course-detail.component';
import { ExtraCoursePopupComponent } from './extra-course-dialog.component';
import { ExtraCourseDeletePopupComponent } from './extra-course-delete-dialog.component';

import { Principal } from '../../shared';

@Injectable()
export class ExtraCourseResolvePagingParams implements Resolve<any> {

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

export const extraCourseRoute: Routes = [
    {
        path: 'extra-course',
        component: ExtraCourseComponent,
        resolve: {
            'pagingParams': ExtraCourseResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.extraCourse.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'extra-course/:id',
        component: ExtraCourseDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.extraCourse.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const extraCoursePopupRoute: Routes = [
    {
        path: 'extra-course-new',
        component: ExtraCoursePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.extraCourse.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'extra-course/:id/edit',
        component: ExtraCoursePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.extraCourse.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'extra-course/:id/delete',
        component: ExtraCourseDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.extraCourse.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
