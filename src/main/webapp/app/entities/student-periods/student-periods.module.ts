import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { KampusSharedModule } from '../../shared';
import {
    StudentPeriodsService,
    StudentPeriodsPopupService,
    StudentPeriodsComponent,
    StudentPeriodsDetailComponent,
    StudentPeriodsDialogComponent,
    StudentPeriodsPopupComponent,
    StudentPeriodsDeletePopupComponent,
    StudentPeriodsDeleteDialogComponent,
    studentPeriodsRoute,
    studentPeriodsPopupRoute,
    StudentPeriodsResolvePagingParams,
    StudentPeriodsAsList,
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
    ...studentPeriodsRoute,
    ...studentPeriodsPopupRoute,
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
        StudentPeriodsComponent,
        StudentPeriodsDetailComponent,
        StudentPeriodsAsList
    ],
    declarations: [
        StudentPeriodsComponent,
        StudentPeriodsDetailComponent,
        StudentPeriodsDialogComponent,
        StudentPeriodsDeleteDialogComponent,
        StudentPeriodsPopupComponent,
        StudentPeriodsDeletePopupComponent,
        StudentPeriodsAsList
    ],
    entryComponents: [
        StudentPeriodsComponent,
        StudentPeriodsDialogComponent,
        StudentPeriodsPopupComponent,
        StudentPeriodsDeleteDialogComponent,
        StudentPeriodsDeletePopupComponent,
        StudentPeriodsAsList
    ],
    providers: [
        StudentPeriodsService,
        StudentPeriodsPopupService,
        StudentPeriodsResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class KampusStudentPeriodsModule {}
