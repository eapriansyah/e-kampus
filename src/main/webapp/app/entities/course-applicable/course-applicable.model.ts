import { BaseEntity } from './../../shared';

export class CourseApplicable implements BaseEntity {
    constructor(
        public id?: any,
        public idApplCourse?: any,
        public dateFrom?: any,
        public dateThru?: any,
        public prodyId?: any,
        public courseId?: any,
        public periodTypeId?: any,
    ) {
    }
}
