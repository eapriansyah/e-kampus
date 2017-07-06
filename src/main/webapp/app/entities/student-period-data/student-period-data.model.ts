import { BaseEntity } from './../../shared';

export class StudentPeriodData implements BaseEntity {
    constructor(
        public id?: any,
        public idStudentPeriod?: any,
        public seqnum?: number,
        public credit?: number,
        public totalCredit?: number,
        public totalPoint?: number,
        public currentPoint?: number,
        public periodId?: any,
        public courseId?: any,
        public studentId?: any,
    ) {
    }
}
