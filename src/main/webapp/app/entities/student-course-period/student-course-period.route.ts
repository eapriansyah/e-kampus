import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { StudentCoursePeriodComponent } from './student-course-period.component';
import { StudentCoursePeriodDetailComponent } from './student-course-period-detail.component';
import { StudentCoursePeriodPopupComponent } from './student-course-period-dialog.component';
import { StudentCoursePeriodDeletePopupComponent } from './student-course-period-delete-dialog.component';

import { Principal } from '../../shared';

@Injectable()
export class StudentCoursePeriodResolvePagingParams implements Resolve<any> {

    constructor(private paginationUtil: JhiPaginationUtil) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const page = route.queryParams['page'] ? route.queryParams['page'] : '1';
        const sort = route.queryParams['sort'] ? route.queryParams['sort'] : 'idStudentCoursePeriod,asc';
        return {
            page: this.paginationUtil.parsePage(page),
            predicate: this.paginationUtil.parsePredicate(sort),
            ascending: this.paginationUtil.parseAscending(sort)
      };
    }
}

export const studentCoursePeriodRoute: Routes = [
    {
        path: 'student-course-period',
        component: StudentCoursePeriodComponent,
        resolve: {
            'pagingParams': StudentCoursePeriodResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.studentCoursePeriod.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'student-course-period/:id',
        component: StudentCoursePeriodDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.studentCoursePeriod.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const studentCoursePeriodPopupRoute: Routes = [
    {
        path: 'student-course-period-new',
        component: StudentCoursePeriodPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.studentCoursePeriod.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'student-course-period/:id/edit',
        component: StudentCoursePeriodPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.studentCoursePeriod.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'student-course-period/:id/delete',
        component: StudentCoursePeriodDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.studentCoursePeriod.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
