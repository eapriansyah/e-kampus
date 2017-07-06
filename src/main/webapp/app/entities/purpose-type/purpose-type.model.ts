import { BaseEntity } from './../../shared';

export class PurposeType implements BaseEntity {
    constructor(
        public id?: number,
        public idPurposeType?: number,
        public description?: string,
    ) {
    }
}
