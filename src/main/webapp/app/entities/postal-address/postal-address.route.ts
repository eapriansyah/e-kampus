import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { PostalAddressComponent } from './postal-address.component';
import { PostalAddressDetailComponent } from './postal-address-detail.component';
import { PostalAddressPopupComponent } from './postal-address-dialog.component';
import { PostalAddressDeletePopupComponent } from './postal-address-delete-dialog.component';

import { Principal } from '../../shared';

@Injectable()
export class PostalAddressResolvePagingParams implements Resolve<any> {

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

export const postalAddressRoute: Routes = [
    {
        path: 'postal-address',
        component: PostalAddressComponent,
        resolve: {
            'pagingParams': PostalAddressResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.postalAddress.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'postal-address/:id',
        component: PostalAddressDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.postalAddress.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const postalAddressPopupRoute: Routes = [
    {
        path: 'postal-address-new',
        component: PostalAddressPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.postalAddress.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'postal-address/:id/edit',
        component: PostalAddressPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.postalAddress.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'postal-address/:id/delete',
        component: PostalAddressDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.postalAddress.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
