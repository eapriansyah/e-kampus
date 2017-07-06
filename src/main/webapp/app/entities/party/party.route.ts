import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { PartyComponent } from './party.component';
import { PartyDetailComponent } from './party-detail.component';
import { PartyPopupComponent } from './party-dialog.component';
import { PartyDeletePopupComponent } from './party-delete-dialog.component';

import { Principal } from '../../shared';

@Injectable()
export class PartyResolvePagingParams implements Resolve<any> {

    constructor(private paginationUtil: JhiPaginationUtil) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const page = route.queryParams['page'] ? route.queryParams['page'] : '1';
        const sort = route.queryParams['sort'] ? route.queryParams['sort'] : 'idParty,asc';
        return {
            page: this.paginationUtil.parsePage(page),
            predicate: this.paginationUtil.parsePredicate(sort),
            ascending: this.paginationUtil.parseAscending(sort)
      };
    }
}

export const partyRoute: Routes = [
    {
        path: 'party',
        component: PartyComponent,
        resolve: {
            'pagingParams': PartyResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.party.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'party/:id',
        component: PartyDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.party.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const partyPopupRoute: Routes = [
    {
        path: 'party-new',
        component: PartyPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.party.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'party/:id/edit',
        component: PartyPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.party.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'party/:id/delete',
        component: PartyDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.party.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
