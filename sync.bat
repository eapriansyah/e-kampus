@echo off
robocopy E:\apps\scm\repo\jhipster\generator-jhipster\generators .\node_modules\generator-jhipster\generators /MIR
call yo jhipster:entity AcademicPeriods --regenerate --force --xskip-server --xskip-client
call yo jhipster:entity Course --regenerate --force --xskip-server --xskip-client
call yo jhipster:entity CourseApplicable --regenerate --force --xskip-server --xskip-client
call yo jhipster:entity CourseLecture --regenerate --force --xskip-server --xskip-client
call yo jhipster:entity ContactMechanism --regenerate --force --xskip-server --xskip-client
call yo jhipster:entity ClassStudy --regenerate --force --xskip-server --xskip-client
call yo jhipster:entity Degree --regenerate --force --xskip-server --xskip-client
call yo jhipster:entity EducationType --regenerate --force --xskip-server --xskip-client
call yo jhipster:entity EventAction --regenerate --force --xskip-server --xskip-client
call yo jhipster:entity ExtraCourse --regenerate --force --xskip-server --xskip-client
call yo jhipster:entity Faculty --regenerate --force --xskip-server --xskip-client
call yo jhipster:entity HostDataSource --regenerate --force --xskip-server --xskip-client
call yo jhipster:entity Internal --regenerate --force --xskip-server --xskip-client
call yo jhipster:entity Lecture --regenerate --force --xskip-server --xskip-client
call yo jhipster:entity OnGoingEvent --regenerate --force --xskip-server --xskip-client
call yo jhipster:entity Organization --regenerate --force --xskip-server --xskip-client
call yo jhipster:entity PeriodType --regenerate --force --xskip-server --xskip-client
call yo jhipster:entity Person --regenerate --force --xskip-server --xskip-client
call yo jhipster:entity PersonalData --regenerate --force --xskip-server --xskip-client
call yo jhipster:entity ProgramStudy --regenerate --force --xskip-server --xskip-client
call yo jhipster:entity RegularCourse --regenerate --force --xskip-server --xskip-client
call yo jhipster:entity ReligionType --regenerate --force --xskip-server --xskip-client
call yo jhipster:entity Student --regenerate --force --xskip-server --xskip-client
call yo jhipster:entity PreStudent --regenerate --force --xskip-server --xskip-client
call yo jhipster:entity PostStudent --regenerate --force --xskip-server --xskip-client
call yo jhipster:entity StudentCoursePeriod --regenerate --force --xskip-server --xskip-client
call yo jhipster:entity StudentCourseScore --regenerate --force --xskip-server --xskip-client
call yo jhipster:entity StudentEvent --regenerate --force --xskip-server --xskip-client
call yo jhipster:entity StudentPeriodData --regenerate --force --xskip-server --xskip-client
call yo jhipster:entity StudentPeriods --regenerate --force --xskip-server --xskip-client
call yo jhipster:entity StudyPath --regenerate --force --xskip-server --xskip-client
call yo jhipster:entity University --regenerate --force --xskip-server --xskip-client
call yo jhipster:entity WorkType --regenerate --force --xskip-server --xskip-client
call yo jhipster:entity ContactMechanismPurpose --regenerate --force --xskip-server --xskip-client
call yo jhipster:entity PostalAddress --regenerate --force --xskip-server --xskip-client
call yo jhipster:entity ElectronicAddress --regenerate --force --xskip-server --xskip-client
call yo jhipster:entity TelecomunicationNumber --regenerate --force --xskip-server --xskip-client
call yo jhipster:entity Building --regenerate --force --xskip-server --xskip-client
call yo jhipster:entity ClassRoom --regenerate --force --xskip-server --xskip-client
call yo jhipster:entity Lab --regenerate --force --xskip-server --xskip-client
call yo jhipster:entity Zone --regenerate --force --xskip-server --xskip-client
call yo jhipster:entity Location --regenerate --force --xskip-server --xskip-client
call yo jhipster:entity Party --regenerate --force --xskip-server --xskip-client
call yo jhipster:entity PurposeType --regenerate --force --xskip-server --xskip-client

