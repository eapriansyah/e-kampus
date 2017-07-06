import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { KampusSharedModule } from '../../shared';
import {
    InternalService,
    InternalPopupService,
    InternalComponent,
    InternalDetailComponent,
    InternalDialogComponent,
    InternalPopupComponent,
    InternalDeletePopupComponent,
    InternalDeleteDialogComponent,
    internalRoute,
    internalPopupRoute,
    InternalResolvePagingParams,
    
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
    ...internalRoute,
    ...internalPopupRoute,
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
        InternalComponent,
        InternalDetailComponent,
        
    ],
    declarations: [
        InternalComponent,
        InternalDetailComponent,
        InternalDialogComponent,
        InternalDeleteDialogComponent,
        InternalPopupComponent,
        InternalDeletePopupComponent,
        
    ],
    entryComponents: [
        InternalComponent,
        InternalDialogComponent,
        InternalPopupComponent,
        InternalDeleteDialogComponent,
        InternalDeletePopupComponent,
        
    ],
    providers: [
        InternalService,
        InternalPopupService,
        InternalResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class KampusInternalModule {}
