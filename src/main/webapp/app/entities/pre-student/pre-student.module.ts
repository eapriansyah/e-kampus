import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { KampusSharedModule } from '../../shared';
import {
    PreStudentService,
    PreStudentPopupService,
    PreStudentComponent,
    PreStudentDetailComponent,
    PreStudentDialogComponent,
    PreStudentPopupComponent,
    PreStudentDeletePopupComponent,
    PreStudentDeleteDialogComponent,
    preStudentRoute,
    preStudentPopupRoute,
    PreStudentResolvePagingParams,
    
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
    ...preStudentRoute,
    ...preStudentPopupRoute,
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
        PreStudentComponent,
        PreStudentDetailComponent,
        
    ],
    declarations: [
        PreStudentComponent,
        PreStudentDetailComponent,
        PreStudentDialogComponent,
        PreStudentDeleteDialogComponent,
        PreStudentPopupComponent,
        PreStudentDeletePopupComponent,
        
    ],
    entryComponents: [
        PreStudentComponent,
        PreStudentDialogComponent,
        PreStudentPopupComponent,
        PreStudentDeleteDialogComponent,
        PreStudentDeletePopupComponent,
        
    ],
    providers: [
        PreStudentService,
        PreStudentPopupService,
        PreStudentResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class KampusPreStudentModule {}
