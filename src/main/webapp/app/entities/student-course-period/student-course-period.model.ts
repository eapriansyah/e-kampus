import { BaseEntity } from './../../shared';

export class StudentCoursePeriod implements BaseEntity {
    constructor(
        public id?: any,
        public idStudentCoursePeriod?: any,
        public credit?: number,
        public value?: number,
        public valueString?: string,
        public status?: number,
        public periodId?: any,
        public courseId?: any,
        public studentId?: any,
    ) {
    }
}
