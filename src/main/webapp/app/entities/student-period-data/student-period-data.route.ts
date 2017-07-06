import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { StudentPeriodDataComponent } from './student-period-data.component';
import { StudentPeriodDataDetailComponent } from './student-period-data-detail.component';
import { StudentPeriodDataPopupComponent } from './student-period-data-dialog.component';
import { StudentPeriodDataDeletePopupComponent } from './student-period-data-delete-dialog.component';

import { Principal } from '../../shared';

@Injectable()
export class StudentPeriodDataResolvePagingParams implements Resolve<any> {

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

export const studentPeriodDataRoute: Routes = [
    {
        path: 'student-period-data',
        component: StudentPeriodDataComponent,
        resolve: {
            'pagingParams': StudentPeriodDataResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.studentPeriodData.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'student-period-data/:id',
        component: StudentPeriodDataDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.studentPeriodData.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const studentPeriodDataPopupRoute: Routes = [
    {
        path: 'student-period-data-new',
        component: StudentPeriodDataPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.studentPeriodData.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'student-period-data/:id/edit',
        component: StudentPeriodDataPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.studentPeriodData.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'student-period-data/:id/delete',
        component: StudentPeriodDataDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.studentPeriodData.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
