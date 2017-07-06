import { BaseEntity } from './../../shared';

export class StudentPeriods implements BaseEntity {
    constructor(
        public id?: any,
        public idStudentPeriod?: any,
        public seqnum?: number,
        public credit?: number,
        public totalCredit?: number,
        public totalPoint?: number,
        public currentPoint?: number,
        public status?: number,
        public studentId?: any,
        public semesterId?: any,
    ) {
    }
}
