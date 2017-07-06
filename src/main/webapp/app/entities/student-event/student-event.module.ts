import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { KampusSharedModule } from '../../shared';
import {
    StudentEventService,
    StudentEventPopupService,
    StudentEventComponent,
    StudentEventDetailComponent,
    StudentEventDialogComponent,
    StudentEventPopupComponent,
    StudentEventDeletePopupComponent,
    StudentEventDeleteDialogComponent,
    studentEventRoute,
    studentEventPopupRoute,
    StudentEventResolvePagingParams,
    StudentEventAsList,
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
    ...studentEventRoute,
    ...studentEventPopupRoute,
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
        StudentEventComponent,
        StudentEventDetailComponent,
        StudentEventAsList
    ],
    declarations: [
        StudentEventComponent,
        StudentEventDetailComponent,
        StudentEventDialogComponent,
        StudentEventDeleteDialogComponent,
        StudentEventPopupComponent,
        StudentEventDeletePopupComponent,
        StudentEventAsList
    ],
    entryComponents: [
        StudentEventComponent,
        StudentEventDialogComponent,
        StudentEventPopupComponent,
        StudentEventDeleteDialogComponent,
        StudentEventDeletePopupComponent,
        StudentEventAsList
    ],
    providers: [
        StudentEventService,
        StudentEventPopupService,
        StudentEventResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class KampusStudentEventModule {}
