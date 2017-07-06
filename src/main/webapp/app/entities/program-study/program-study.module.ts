import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { KampusSharedModule } from '../../shared';
import {
    ProgramStudyService,
    ProgramStudyPopupService,
    ProgramStudyComponent,
    ProgramStudyDetailComponent,
    ProgramStudyDialogComponent,
    ProgramStudyPopupComponent,
    ProgramStudyDeletePopupComponent,
    ProgramStudyDeleteDialogComponent,
    programStudyRoute,
    programStudyPopupRoute,
    ProgramStudyResolvePagingParams,
    
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
    ...programStudyRoute,
    ...programStudyPopupRoute,
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
        ProgramStudyComponent,
        ProgramStudyDetailComponent,
        
    ],
    declarations: [
        ProgramStudyComponent,
        ProgramStudyDetailComponent,
        ProgramStudyDialogComponent,
        ProgramStudyDeleteDialogComponent,
        ProgramStudyPopupComponent,
        ProgramStudyDeletePopupComponent,
        
    ],
    entryComponents: [
        ProgramStudyComponent,
        ProgramStudyDialogComponent,
        ProgramStudyPopupComponent,
        ProgramStudyDeleteDialogComponent,
        ProgramStudyDeletePopupComponent,
        
    ],
    providers: [
        ProgramStudyService,
        ProgramStudyPopupService,
        ProgramStudyResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class KampusProgramStudyModule {}
