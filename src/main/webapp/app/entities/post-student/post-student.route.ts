import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { PostStudentComponent } from './post-student.component';
import { PostStudentDetailComponent } from './post-student-detail.component';
import { PostStudentPopupComponent } from './post-student-dialog.component';
import { PostStudentDeletePopupComponent } from './post-student-delete-dialog.component';

import { Principal } from '../../shared';

@Injectable()
export class PostStudentResolvePagingParams implements Resolve<any> {

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

export const postStudentRoute: Routes = [
    {
        path: 'post-student',
        component: PostStudentComponent,
        resolve: {
            'pagingParams': PostStudentResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.postStudent.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'post-student/:id',
        component: PostStudentDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.postStudent.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const postStudentPopupRoute: Routes = [
    {
        path: 'post-student-new',
        component: PostStudentPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.postStudent.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'post-student/:id/edit',
        component: PostStudentPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.postStudent.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'post-student/:id/delete',
        component: PostStudentDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.postStudent.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
