import { BaseEntity } from './../../shared';

export class Building implements BaseEntity {
    constructor(
        public id?: any,
        public idFacility?: any,
        public facilityCode?: string,
        public description?: string,
        public zoneId?: any,
    ) {
    }
}
