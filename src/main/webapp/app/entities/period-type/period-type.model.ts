import { BaseEntity } from './../../shared';

export class PeriodType implements BaseEntity {
    constructor(
        public id?: number,
        public idPeriodType?: number,
        public description?: string,
        public fromDay?: number,
        public fromMonth?: number,
        public thruDay?: number,
        public thruMonth?: number,
        public fromAddYear?: number,
        public thruAddYear?: number,
        public sequence?: number,
        public idParent?: number,
    ) {
    }
}
