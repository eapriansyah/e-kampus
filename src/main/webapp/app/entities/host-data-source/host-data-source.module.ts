import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { KampusSharedModule } from '../../shared';
import {
    HostDataSourceService,
    HostDataSourcePopupService,
    HostDataSourceComponent,
    HostDataSourceDetailComponent,
    HostDataSourceDialogComponent,
    HostDataSourcePopupComponent,
    HostDataSourceDeletePopupComponent,
    HostDataSourceDeleteDialogComponent,
    hostDataSourceRoute,
    hostDataSourcePopupRoute,
    HostDataSourceResolvePagingParams,
    
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
    ...hostDataSourceRoute,
    ...hostDataSourcePopupRoute,
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
        HostDataSourceComponent,
        HostDataSourceDetailComponent,
        
    ],
    declarations: [
        HostDataSourceComponent,
        HostDataSourceDetailComponent,
        HostDataSourceDialogComponent,
        HostDataSourceDeleteDialogComponent,
        HostDataSourcePopupComponent,
        HostDataSourceDeletePopupComponent,
        
    ],
    entryComponents: [
        HostDataSourceComponent,
        HostDataSourceDialogComponent,
        HostDataSourcePopupComponent,
        HostDataSourceDeleteDialogComponent,
        HostDataSourceDeletePopupComponent,
        
    ],
    providers: [
        HostDataSourceService,
        HostDataSourcePopupService,
        HostDataSourceResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class KampusHostDataSourceModule {}
