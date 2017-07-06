import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { KampusSharedModule } from '../../shared';
import {
    StudentCourseScoreService,
    StudentCourseScorePopupService,
    StudentCourseScoreComponent,
    StudentCourseScoreDetailComponent,
    StudentCourseScoreDialogComponent,
    StudentCourseScorePopupComponent,
    StudentCourseScoreDeletePopupComponent,
    StudentCourseScoreDeleteDialogComponent,
    studentCourseScoreRoute,
    studentCourseScorePopupRoute,
    StudentCourseScoreResolvePagingParams,
    StudentCourseScoreAsList,
} from './';

import {AutoCompleteModule,
        CheckboxModule,
        InputTextModule,
        CalendarModule,
        DropdownModule,
        ButtonModule,
        DataTableModule,
        PaginatorModule,
        ConfirmDialogModule,
        ConfirmationService,
        GrowlModule,
        DataGridModule,
        SharedModule,
        AccordionModule,
        TabViewModule,
        FieldsetModule,
        ScheduleModule,
        PanelModule,
        MenuItem,
        Header,
        Footer} from 'primeng/primeng';

const ENTITY_STATES = [
    ...studentCourseScoreRoute,
    ...studentCourseScorePopupRoute,
];

@NgModule({
    imports: [
        KampusSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true }),
        ButtonModule,
        DataTableModule,
        PaginatorModule,
        ConfirmDialogModule,
        TabViewModule,
        DataGridModule,
        ScheduleModule,
        AutoCompleteModule,
        CheckboxModule,
        CalendarModule,
        DropdownModule,
        FieldsetModule,
        PanelModule
    ],
    exports: [
        StudentCourseScoreComponent,
        StudentCourseScoreDetailComponent,
        StudentCourseScoreAsList
    ],
    declarations: [
        StudentCourseScoreComponent,
        StudentCourseScoreDetailComponent,
        StudentCourseScoreDialogComponent,
        StudentCourseScoreDeleteDialogComponent,
        StudentCourseScorePopupComponent,
        StudentCourseScoreDeletePopupComponent,
        StudentCourseScoreAsList
    ],
    entryComponents: [
        StudentCourseScoreComponent,
        StudentCourseScoreDialogComponent,
        StudentCourseScorePopupComponent,
        StudentCourseScoreDeleteDialogComponent,
        StudentCourseScoreDeletePopupComponent,
        StudentCourseScoreAsList
    ],
    providers: [
        StudentCourseScoreService,
        StudentCourseScorePopupService,
        StudentCourseScoreResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class KampusStudentCourseScoreModule {}
