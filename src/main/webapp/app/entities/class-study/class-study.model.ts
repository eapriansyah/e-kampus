import { BaseEntity } from './../../shared';

export class ClassStudy implements BaseEntity {
    constructor(
        public id?: any,
        public idClassStudy?: any,
        public description?: string,
        public refkey?: string,
        public courseId?: any,
        public prodyId?: any,
        public periodId?: any,
        public lectureId?: any,
        public secondaryLectureId?: any,
        public thirdLectureId?: any,
    ) {
    }
}
