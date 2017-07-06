import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { KampusSharedModule } from '../../shared';
import {
    WorkTypeService,
    WorkTypePopupService,
    WorkTypeComponent,
    WorkTypeDetailComponent,
    WorkTypeDialogComponent,
    WorkTypePopupComponent,
    WorkTypeDeletePopupComponent,
    WorkTypeDeleteDialogComponent,
    workTypeRoute,
    workTypePopupRoute,
    WorkTypeResolvePagingParams,
    
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
    ...workTypeRoute,
    ...workTypePopupRoute,
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
        WorkTypeComponent,
        WorkTypeDetailComponent,
        
    ],
    declarations: [
        WorkTypeComponent,
        WorkTypeDetailComponent,
        WorkTypeDialogComponent,
        WorkTypeDeleteDialogComponent,
        WorkTypePopupComponent,
        WorkTypeDeletePopupComponent,
        
    ],
    entryComponents: [
        WorkTypeComponent,
        WorkTypeDialogComponent,
        WorkTypePopupComponent,
        WorkTypeDeleteDialogComponent,
        WorkTypeDeletePopupComponent,
        
    ],
    providers: [
        WorkTypeService,
        WorkTypePopupService,
        WorkTypeResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class KampusWorkTypeModule {}
