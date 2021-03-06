import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { KampusSharedModule } from '../../shared';
import {
    BuildingService,
    BuildingPopupService,
    BuildingComponent,
    BuildingDetailComponent,
    BuildingDialogComponent,
    BuildingPopupComponent,
    BuildingDeletePopupComponent,
    BuildingDeleteDialogComponent,
    buildingRoute,
    buildingPopupRoute,
    BuildingResolvePagingParams,
    
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
    ...buildingRoute,
    ...buildingPopupRoute,
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
        BuildingComponent,
        BuildingDetailComponent,
        
    ],
    declarations: [
        BuildingComponent,
        BuildingDetailComponent,
        BuildingDialogComponent,
        BuildingDeleteDialogComponent,
        BuildingPopupComponent,
        BuildingDeletePopupComponent,
        
    ],
    entryComponents: [
        BuildingComponent,
        BuildingDialogComponent,
        BuildingPopupComponent,
        BuildingDeleteDialogComponent,
        BuildingDeletePopupComponent,
        
    ],
    providers: [
        BuildingService,
        BuildingPopupService,
        BuildingResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class KampusBuildingModule {}
