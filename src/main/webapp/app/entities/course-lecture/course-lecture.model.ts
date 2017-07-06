import { BaseEntity } from './../../shared';

export class CourseLecture implements BaseEntity {
    constructor(
        public id?: any,
        public idCourseLecture?: any,
        public dateFrom?: any,
        public dateThru?: any,
        public lectureId?: any,
        public courseId?: any,
    ) {
    }
}
