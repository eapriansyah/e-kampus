import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { KampusAcademicPeriodsModule } from './academic-periods/academic-periods.module';
import { KampusCourseModule } from './course/course.module';
import { KampusCourseApplicableModule } from './course-applicable/course-applicable.module';
import { KampusCourseLectureModule } from './course-lecture/course-lecture.module';
import { KampusContactMechanismModule } from './contact-mechanism/contact-mechanism.module';
import { KampusClassStudyModule } from './class-study/class-study.module';
import { KampusDegreeModule } from './degree/degree.module';
import { KampusEducationTypeModule } from './education-type/education-type.module';
import { KampusEventActionModule } from './event-action/event-action.module';
import { KampusExtraCourseModule } from './extra-course/extra-course.module';
import { KampusFacultyModule } from './faculty/faculty.module';
import { KampusHostDataSourceModule } from './host-data-source/host-data-source.module';
import { KampusInternalModule } from './internal/internal.module';
import { KampusLectureModule } from './lecture/lecture.module';
import { KampusOnGoingEventModule } from './on-going-event/on-going-event.module';
import { KampusOrganizationModule } from './organization/organization.module';
import { KampusPeriodTypeModule } from './period-type/period-type.module';
import { KampusPersonModule } from './person/person.module';
import { KampusPersonalDataModule } from './personal-data/personal-data.module';
import { KampusProgramStudyModule } from './program-study/program-study.module';
import { KampusRegularCourseModule } from './regular-course/regular-course.module';
import { KampusReligionTypeModule } from './religion-type/religion-type.module';
import { KampusStudentModule } from './student/student.module';
import { KampusPreStudentModule } from './pre-student/pre-student.module';
import { KampusPostStudentModule } from './post-student/post-student.module';
import { KampusStudentCoursePeriodModule } from './student-course-period/student-course-period.module';
import { KampusStudentCourseScoreModule } from './student-course-score/student-course-score.module';
import { KampusStudentEventModule } from './student-event/student-event.module';
import { KampusStudentPeriodDataModule } from './student-period-data/student-period-data.module';
import { KampusStudentPeriodsModule } from './student-periods/student-periods.module';
import { KampusStudyPathModule } from './study-path/study-path.module';
import { KampusUniversityModule } from './university/university.module';
import { KampusWorkTypeModule } from './work-type/work-type.module';
import { KampusContactMechanismPurposeModule } from './contact-mechanism-purpose/contact-mechanism-purpose.module';
import { KampusPostalAddressModule } from './postal-address/postal-address.module';
import { KampusElectronicAddressModule } from './electronic-address/electronic-address.module';
import { KampusTelecomunicationNumberModule } from './telecomunication-number/telecomunication-number.module';
import { KampusBuildingModule } from './building/building.module';
import { KampusClassRoomModule } from './class-room/class-room.module';
import { KampusLabModule } from './lab/lab.module';
import { KampusZoneModule } from './zone/zone.module';
import { KampusLocationModule } from './location/location.module';
import { KampusPartyModule } from './party/party.module';
import { KampusPurposeTypeModule } from './purpose-type/purpose-type.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        KampusAcademicPeriodsModule,
        KampusCourseModule,
        KampusCourseApplicableModule,
        KampusCourseLectureModule,
        KampusContactMechanismModule,
        KampusClassStudyModule,
        KampusDegreeModule,
        KampusEducationTypeModule,
        KampusEventActionModule,
        KampusExtraCourseModule,
        KampusFacultyModule,
        KampusHostDataSourceModule,
        KampusInternalModule,
        KampusLectureModule,
        KampusOnGoingEventModule,
        KampusOrganizationModule,
        KampusPeriodTypeModule,
        KampusPersonModule,
        KampusPersonalDataModule,
        KampusProgramStudyModule,
        KampusRegularCourseModule,
        KampusReligionTypeModule,
        KampusStudentModule,
        KampusPreStudentModule,
        KampusPostStudentModule,
        KampusStudentCoursePeriodModule,
        KampusStudentCourseScoreModule,
        KampusStudentEventModule,
        KampusStudentPeriodDataModule,
        KampusStudentPeriodsModule,
        KampusStudyPathModule,
        KampusUniversityModule,
        KampusWorkTypeModule,
        KampusContactMechanismPurposeModule,
        KampusPostalAddressModule,
        KampusElectronicAddressModule,
        KampusTelecomunicationNumberModule,
        KampusBuildingModule,
        KampusClassRoomModule,
        KampusLabModule,
        KampusZoneModule,
        KampusLocationModule,
        KampusPartyModule,
        KampusPurposeTypeModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class KampusEntityModule {}
