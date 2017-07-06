import { BaseEntity } from './../../shared';

export class StudentCourseScore implements BaseEntity {
    constructor(
        public id?: any,
        public idStudentCourseScore?: any,
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
