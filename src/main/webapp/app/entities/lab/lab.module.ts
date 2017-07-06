import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { KampusSharedModule } from '../../shared';
import {
    LabService,
    LabPopupService,
    LabComponent,
    LabDetailComponent,
    LabDialogComponent,
    LabPopupComponent,
    LabDeletePopupComponent,
    LabDeleteDialogComponent,
    labRoute,
    labPopupRoute,
    LabResolvePagingParams,
    
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
    ...labRoute,
    ...labPopupRoute,
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
        LabComponent,
        LabDetailComponent,
        
    ],
    declarations: [
        LabComponent,
        LabDetailComponent,
        LabDialogComponent,
        LabDeleteDialogComponent,
        LabPopupComponent,
        LabDeletePopupComponent,
        
    ],
    entryComponents: [
        LabComponent,
        LabDialogComponent,
        LabPopupComponent,
        LabDeleteDialogComponent,
        LabDeletePopupComponent,
        
    ],
    providers: [
        LabService,
        LabPopupService,
        LabResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class KampusLabModule {}
