import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { ClassStudyComponent } from './class-study.component';
import { ClassStudyDetailComponent } from './class-study-detail.component';
import { ClassStudyPopupComponent } from './class-study-dialog.component';
import { ClassStudyDeletePopupComponent } from './class-study-delete-dialog.component';

import { Principal } from '../../shared';

@Injectable()
export class ClassStudyResolvePagingParams implements Resolve<any> {

    constructor(private paginationUtil: JhiPaginationUtil) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const page = route.queryParams['page'] ? route.queryParams['page'] : '1';
        const sort = route.queryParams['sort'] ? route.queryParams['sort'] : 'idClassStudy,asc';
        return {
            page: this.paginationUtil.parsePage(page),
            predicate: this.paginationUtil.parsePredicate(sort),
            ascending: this.paginationUtil.parseAscending(sort)
      };
    }
}

export const classStudyRoute: Routes = [
    {
        path: 'class-study',
        component: ClassStudyComponent,
        resolve: {
            'pagingParams': ClassStudyResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.classStudy.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'class-study/:id',
        component: ClassStudyDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.classStudy.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const classStudyPopupRoute: Routes = [
    {
        path: 'class-study-new',
        component: ClassStudyPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.classStudy.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'class-study/:id/edit',
        component: ClassStudyPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.classStudy.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'class-study/:id/delete',
        component: ClassStudyDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.classStudy.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
