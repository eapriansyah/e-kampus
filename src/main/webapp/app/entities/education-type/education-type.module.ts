import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { KampusSharedModule } from '../../shared';
import {
    EducationTypeService,
    EducationTypePopupService,
    EducationTypeComponent,
    EducationTypeDetailComponent,
    EducationTypeDialogComponent,
    EducationTypePopupComponent,
    EducationTypeDeletePopupComponent,
    EducationTypeDeleteDialogComponent,
    educationTypeRoute,
    educationTypePopupRoute,
    EducationTypeResolvePagingParams,
    
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
    ...educationTypeRoute,
    ...educationTypePopupRoute,
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
        EducationTypeComponent,
        EducationTypeDetailComponent,
        
    ],
    declarations: [
        EducationTypeComponent,
        EducationTypeDetailComponent,
        EducationTypeDialogComponent,
        EducationTypeDeleteDialogComponent,
        EducationTypePopupComponent,
        EducationTypeDeletePopupComponent,
        
    ],
    entryComponents: [
        EducationTypeComponent,
        EducationTypeDialogComponent,
        EducationTypePopupComponent,
        EducationTypeDeleteDialogComponent,
        EducationTypeDeletePopupComponent,
        
    ],
    providers: [
        EducationTypeService,
        EducationTypePopupService,
        EducationTypeResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class KampusEducationTypeModule {}
