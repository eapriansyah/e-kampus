import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { TelecomunicationNumberComponent } from './telecomunication-number.component';
import { TelecomunicationNumberDetailComponent } from './telecomunication-number-detail.component';
import { TelecomunicationNumberPopupComponent } from './telecomunication-number-dialog.component';
import { TelecomunicationNumberDeletePopupComponent } from './telecomunication-number-delete-dialog.component';

import { Principal } from '../../shared';

@Injectable()
export class TelecomunicationNumberResolvePagingParams implements Resolve<any> {

    constructor(private paginationUtil: JhiPaginationUtil) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const page = route.queryParams['page'] ? route.queryParams['page'] : '1';
        const sort = route.queryParams['sort'] ? route.queryParams['sort'] : 'idContact,asc';
        return {
            page: this.paginationUtil.parsePage(page),
            predicate: this.paginationUtil.parsePredicate(sort),
            ascending: this.paginationUtil.parseAscending(sort)
      };
    }
}

export const telecomunicationNumberRoute: Routes = [
    {
        path: 'telecomunication-number',
        component: TelecomunicationNumberComponent,
        resolve: {
            'pagingParams': TelecomunicationNumberResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.telecomunicationNumber.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'telecomunication-number/:id',
        component: TelecomunicationNumberDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.telecomunicationNumber.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const telecomunicationNumberPopupRoute: Routes = [
    {
        path: 'telecomunication-number-new',
        component: TelecomunicationNumberPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.telecomunicationNumber.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'telecomunication-number/:id/edit',
        component: TelecomunicationNumberPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.telecomunicationNumber.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'telecomunication-number/:id/delete',
        component: TelecomunicationNumberDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.telecomunicationNumber.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
