import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { ClassRoomComponent } from './class-room.component';
import { ClassRoomDetailComponent } from './class-room-detail.component';
import { ClassRoomPopupComponent } from './class-room-dialog.component';
import { ClassRoomDeletePopupComponent } from './class-room-delete-dialog.component';

import { Principal } from '../../shared';

@Injectable()
export class ClassRoomResolvePagingParams implements Resolve<any> {

    constructor(private paginationUtil: JhiPaginationUtil) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const page = route.queryParams['page'] ? route.queryParams['page'] : '1';
        const sort = route.queryParams['sort'] ? route.queryParams['sort'] : 'idFacility,asc';
        return {
            page: this.paginationUtil.parsePage(page),
            predicate: this.paginationUtil.parsePredicate(sort),
            ascending: this.paginationUtil.parseAscending(sort)
      };
    }
}

export const classRoomRoute: Routes = [
    {
        path: 'class-room',
        component: ClassRoomComponent,
        resolve: {
            'pagingParams': ClassRoomResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.classRoom.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'class-room/:id',
        component: ClassRoomDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.classRoom.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const classRoomPopupRoute: Routes = [
    {
        path: 'class-room-new',
        component: ClassRoomPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.classRoom.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'class-room/:id/edit',
        component: ClassRoomPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.classRoom.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'class-room/:id/delete',
        component: ClassRoomDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.classRoom.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
