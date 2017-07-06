import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { KampusSharedModule } from '../../shared';
import {
    ZoneService,
    ZonePopupService,
    ZoneComponent,
    ZoneDetailComponent,
    ZoneDialogComponent,
    ZonePopupComponent,
    ZoneDeletePopupComponent,
    ZoneDeleteDialogComponent,
    zoneRoute,
    zonePopupRoute,
    ZoneResolvePagingParams,
    
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
    ...zoneRoute,
    ...zonePopupRoute,
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
        ZoneComponent,
        ZoneDetailComponent,
        
    ],
    declarations: [
        ZoneComponent,
        ZoneDetailComponent,
        ZoneDialogComponent,
        ZoneDeleteDialogComponent,
        ZonePopupComponent,
        ZoneDeletePopupComponent,
        
    ],
    entryComponents: [
        ZoneComponent,
        ZoneDialogComponent,
        ZonePopupComponent,
        ZoneDeleteDialogComponent,
        ZoneDeletePopupComponent,
        
    ],
    providers: [
        ZoneService,
        ZonePopupService,
        ZoneResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class KampusZoneModule {}
