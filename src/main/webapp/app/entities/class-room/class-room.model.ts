import { BaseEntity } from './../../shared';

export class ClassRoom implements BaseEntity {
    constructor(
        public id?: any,
        public idFacility?: any,
        public facilityCode?: string,
        public description?: string,
        public buildingId?: any,
    ) {
    }
}
