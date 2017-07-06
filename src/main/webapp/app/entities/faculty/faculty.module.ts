import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { KampusSharedModule } from '../../shared';
import {
    FacultyService,
    FacultyPopupService,
    FacultyComponent,
    FacultyDetailComponent,
    FacultyDialogComponent,
    FacultyPopupComponent,
    FacultyDeletePopupComponent,
    FacultyDeleteDialogComponent,
    facultyRoute,
    facultyPopupRoute,
    FacultyResolvePagingParams,
    
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
    ...facultyRoute,
    ...facultyPopupRoute,
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
        FacultyComponent,
        FacultyDetailComponent,
        
    ],
    declarations: [
        FacultyComponent,
        FacultyDetailComponent,
        FacultyDialogComponent,
        FacultyDeleteDialogComponent,
        FacultyPopupComponent,
        FacultyDeletePopupComponent,
        
    ],
    entryComponents: [
        FacultyComponent,
        FacultyDialogComponent,
        FacultyPopupComponent,
        FacultyDeleteDialogComponent,
        FacultyDeletePopupComponent,
        
    ],
    providers: [
        FacultyService,
        FacultyPopupService,
        FacultyResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class KampusFacultyModule {}
