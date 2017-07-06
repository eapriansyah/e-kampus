import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { StudentCourseScoreComponent } from './student-course-score.component';
import { StudentCourseScoreDetailComponent } from './student-course-score-detail.component';
import { StudentCourseScorePopupComponent } from './student-course-score-dialog.component';
import { StudentCourseScoreDeletePopupComponent } from './student-course-score-delete-dialog.component';

import { Principal } from '../../shared';

@Injectable()
export class StudentCourseScoreResolvePagingParams implements Resolve<any> {

    constructor(private paginationUtil: JhiPaginationUtil) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const page = route.queryParams['page'] ? route.queryParams['page'] : '1';
        const sort = route.queryParams['sort'] ? route.queryParams['sort'] : 'idStudentCourseScore,asc';
        return {
            page: this.paginationUtil.parsePage(page),
            predicate: this.paginationUtil.parsePredicate(sort),
            ascending: this.paginationUtil.parseAscending(sort)
      };
    }
}

export const studentCourseScoreRoute: Routes = [
    {
        path: 'student-course-score',
        component: StudentCourseScoreComponent,
        resolve: {
            'pagingParams': StudentCourseScoreResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.studentCourseScore.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'student-course-score/:id',
        component: StudentCourseScoreDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.studentCourseScore.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const studentCourseScorePopupRoute: Routes = [
    {
        path: 'student-course-score-new',
        component: StudentCourseScorePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.studentCourseScore.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'student-course-score/:id/edit',
        component: StudentCourseScorePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.studentCourseScore.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'student-course-score/:id/delete',
        component: StudentCourseScoreDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.studentCourseScore.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
