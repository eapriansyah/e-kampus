import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { PersonalDataComponent } from './personal-data.component';
import { PersonalDataDetailComponent } from './personal-data-detail.component';
import { PersonalDataPopupComponent } from './personal-data-dialog.component';
import { PersonalDataDeletePopupComponent } from './personal-data-delete-dialog.component';

import { Principal } from '../../shared';

@Injectable()
export class PersonalDataResolvePagingParams implements Resolve<any> {

    constructor(private paginationUtil: JhiPaginationUtil) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const page = route.queryParams['page'] ? route.queryParams['page'] : '1';
        const sort = route.queryParams['sort'] ? route.queryParams['sort'] : 'idPersonalData,asc';
        return {
            page: this.paginationUtil.parsePage(page),
            predicate: this.paginationUtil.parsePredicate(sort),
            ascending: this.paginationUtil.parseAscending(sort)
      };
    }
}

export const personalDataRoute: Routes = [
    {
        path: 'personal-data',
        component: PersonalDataComponent,
        resolve: {
            'pagingParams': PersonalDataResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.personalData.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'personal-data/:id',
        component: PersonalDataDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.personalData.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const personalDataPopupRoute: Routes = [
    {
        path: 'personal-data-new',
        component: PersonalDataPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.personalData.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'personal-data/:id/edit',
        component: PersonalDataPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.personalData.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'personal-data/:id/delete',
        component: PersonalDataDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.personalData.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
