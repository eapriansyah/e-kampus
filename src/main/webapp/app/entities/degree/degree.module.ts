import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { KampusSharedModule } from '../../shared';
import {
    DegreeService,
    DegreePopupService,
    DegreeComponent,
    DegreeDetailComponent,
    DegreeDialogComponent,
    DegreePopupComponent,
    DegreeDeletePopupComponent,
    DegreeDeleteDialogComponent,
    degreeRoute,
    degreePopupRoute,
    DegreeResolvePagingParams,
    
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
    ...degreeRoute,
    ...degreePopupRoute,
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
        DegreeComponent,
        DegreeDetailComponent,
        
    ],
    declarations: [
        DegreeComponent,
        DegreeDetailComponent,
        DegreeDialogComponent,
        DegreeDeleteDialogComponent,
        DegreePopupComponent,
        DegreeDeletePopupComponent,
        
    ],
    entryComponents: [
        DegreeComponent,
        DegreeDialogComponent,
        DegreePopupComponent,
        DegreeDeleteDialogComponent,
        DegreeDeletePopupComponent,
        
    ],
    providers: [
        DegreeService,
        DegreePopupService,
        DegreeResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class KampusDegreeModule {}
