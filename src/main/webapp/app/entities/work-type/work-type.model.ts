import { BaseEntity } from './../../shared';

export class WorkType implements BaseEntity {
    constructor(
        public id?: number,
        public idWorkType?: number,
        public description?: string,
    ) {
    }
}
