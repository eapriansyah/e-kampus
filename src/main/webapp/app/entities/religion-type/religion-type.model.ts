import { BaseEntity } from './../../shared';

export class ReligionType implements BaseEntity {
    constructor(
        public id?: number,
        public idReligionType?: number,
        public description?: string,
    ) {
    }
}
