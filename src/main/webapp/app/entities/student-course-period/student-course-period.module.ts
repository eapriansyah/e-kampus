import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { KampusSharedModule } from '../../shared';
import {
    StudentCoursePeriodService,
    StudentCoursePeriodPopupService,
    StudentCoursePeriodComponent,
    StudentCoursePeriodDetailComponent,
    StudentCoursePeriodDialogComponent,
    StudentCoursePeriodPopupComponent,
    StudentCoursePeriodDeletePopupComponent,
    StudentCoursePeriodDeleteDialogComponent,
    studentCoursePeriodRoute,
    studentCoursePeriodPopupRoute,
    StudentCoursePeriodResolvePagingParams,
    StudentCoursePeriodAsList,
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
    ...studentCoursePeriodRoute,
    ...studentCoursePeriodPopupRoute,
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
        StudentCoursePeriodComponent,
        StudentCoursePeriodDetailComponent,
        StudentCoursePeriodAsList
    ],
    declarations: [
        StudentCoursePeriodComponent,
        StudentCoursePeriodDetailComponent,
        StudentCoursePeriodDialogComponent,
        StudentCoursePeriodDeleteDialogComponent,
        StudentCoursePeriodPopupComponent,
        StudentCoursePeriodDeletePopupComponent,
        StudentCoursePeriodAsList
    ],
    entryComponents: [
        StudentCoursePeriodComponent,
        StudentCoursePeriodDialogComponent,
        StudentCoursePeriodPopupComponent,
        StudentCoursePeriodDeleteDialogComponent,
        StudentCoursePeriodDeletePopupComponent,
        StudentCoursePeriodAsList
    ],
    providers: [
        StudentCoursePeriodService,
        StudentCoursePeriodPopupService,
        StudentCoursePeriodResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class KampusStudentCoursePeriodModule {}
