import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { KampusSharedModule } from '../../shared';
import {
    TelecomunicationNumberService,
    TelecomunicationNumberPopupService,
    TelecomunicationNumberComponent,
    TelecomunicationNumberDetailComponent,
    TelecomunicationNumberDialogComponent,
    TelecomunicationNumberPopupComponent,
    TelecomunicationNumberDeletePopupComponent,
    TelecomunicationNumberDeleteDialogComponent,
    telecomunicationNumberRoute,
    telecomunicationNumberPopupRoute,
    TelecomunicationNumberResolvePagingParams,
    
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
    ...telecomunicationNumberRoute,
    ...telecomunicationNumberPopupRoute,
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
        TelecomunicationNumberComponent,
        TelecomunicationNumberDetailComponent,
        
    ],
    declarations: [
        TelecomunicationNumberComponent,
        TelecomunicationNumberDetailComponent,
        TelecomunicationNumberDialogComponent,
        TelecomunicationNumberDeleteDialogComponent,
        TelecomunicationNumberPopupComponent,
        TelecomunicationNumberDeletePopupComponent,
        
    ],
    entryComponents: [
        TelecomunicationNumberComponent,
        TelecomunicationNumberDialogComponent,
        TelecomunicationNumberPopupComponent,
        TelecomunicationNumberDeleteDialogComponent,
        TelecomunicationNumberDeletePopupComponent,
        
    ],
    providers: [
        TelecomunicationNumberService,
        TelecomunicationNumberPopupService,
        TelecomunicationNumberResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class KampusTelecomunicationNumberModule {}
