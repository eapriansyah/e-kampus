import { BaseEntity } from './../../shared';

export class Location implements BaseEntity {
    constructor(
        public id?: any,
        public idGeoBoundary?: any,
        public geoCode?: string,
        public description?: string,
    ) {
    }
}
