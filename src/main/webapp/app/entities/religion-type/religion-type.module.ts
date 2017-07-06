import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { KampusSharedModule } from '../../shared';
import {
    ReligionTypeService,
    ReligionTypePopupService,
    ReligionTypeComponent,
    ReligionTypeDetailComponent,
    ReligionTypeDialogComponent,
    ReligionTypePopupComponent,
    ReligionTypeDeletePopupComponent,
    ReligionTypeDeleteDialogComponent,
    religionTypeRoute,
    religionTypePopupRoute,
    ReligionTypeResolvePagingParams,
    
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
    ...religionTypeRoute,
    ...religionTypePopupRoute,
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
        ReligionTypeComponent,
        ReligionTypeDetailComponent,
        
    ],
    declarations: [
        ReligionTypeComponent,
        ReligionTypeDetailComponent,
        ReligionTypeDialogComponent,
        ReligionTypeDeleteDialogComponent,
        ReligionTypePopupComponent,
        ReligionTypeDeletePopupComponent,
        
    ],
    entryComponents: [
        ReligionTypeComponent,
        ReligionTypeDialogComponent,
        ReligionTypePopupComponent,
        ReligionTypeDeleteDialogComponent,
        ReligionTypeDeletePopupComponent,
        
    ],
    providers: [
        ReligionTypeService,
        ReligionTypePopupService,
        ReligionTypeResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class KampusReligionTypeModule {}
