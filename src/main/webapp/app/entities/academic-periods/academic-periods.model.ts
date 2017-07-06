import { BaseEntity } from './../../shared';

export class AcademicPeriods implements BaseEntity {
    constructor(
        public id?: any,
        public idPeriod?: any,
        public description?: string,
        public year?: number,
        public format1?: string,
        public dateFrom?: any,
        public dateThru?: any,
        public periodTypeId?: any,
    ) {
    }
}
