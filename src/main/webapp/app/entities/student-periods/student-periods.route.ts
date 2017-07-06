import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { StudentPeriodsComponent } from './student-periods.component';
import { StudentPeriodsDetailComponent } from './student-periods-detail.component';
import { StudentPeriodsPopupComponent } from './student-periods-dialog.component';
import { StudentPeriodsDeletePopupComponent } from './student-periods-delete-dialog.component';

import { Principal } from '../../shared';

@Injectable()
export class StudentPeriodsResolvePagingParams implements Resolve<any> {

    constructor(private paginationUtil: JhiPaginationUtil) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const page = route.queryParams['page'] ? route.queryParams['page'] : '1';
        const sort = route.queryParams['sort'] ? route.queryParams['sort'] : 'idStudentPeriod,asc';
        return {
            page: this.paginationUtil.parsePage(page),
            predicate: this.paginationUtil.parsePredicate(sort),
            ascending: this.paginationUtil.parseAscending(sort)
      };
    }
}

export const studentPeriodsRoute: Routes = [
    {
        path: 'student-periods',
        component: StudentPeriodsComponent,
        resolve: {
            'pagingParams': StudentPeriodsResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.studentPeriods.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'student-periods/:id',
        component: StudentPeriodsDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.studentPeriods.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const studentPeriodsPopupRoute: Routes = [
    {
        path: 'student-periods-new',
        component: StudentPeriodsPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.studentPeriods.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'student-periods/:id/edit',
        component: StudentPeriodsPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.studentPeriods.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'student-periods/:id/delete',
        component: StudentPeriodsDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.studentPeriods.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
