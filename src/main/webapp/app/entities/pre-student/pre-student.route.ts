import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { PreStudentComponent } from './pre-student.component';
import { PreStudentDetailComponent } from './pre-student-detail.component';
import { PreStudentPopupComponent } from './pre-student-dialog.component';
import { PreStudentDeletePopupComponent } from './pre-student-delete-dialog.component';

import { Principal } from '../../shared';

@Injectable()
export class PreStudentResolvePagingParams implements Resolve<any> {

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

export const preStudentRoute: Routes = [
    {
        path: 'pre-student',
        component: PreStudentComponent,
        resolve: {
            'pagingParams': PreStudentResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.preStudent.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'pre-student/:id',
        component: PreStudentDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.preStudent.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const preStudentPopupRoute: Routes = [
    {
        path: 'pre-student-new',
        component: PreStudentPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.preStudent.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'pre-student/:id/edit',
        component: PreStudentPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.preStudent.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'pre-student/:id/delete',
        component: PreStudentDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.preStudent.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
