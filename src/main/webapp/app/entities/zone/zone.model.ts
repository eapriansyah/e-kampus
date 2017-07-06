import { BaseEntity } from './../../shared';

export class Zone implements BaseEntity {
    constructor(
        public id?: any,
        public idGeoBoundary?: any,
        public geoCode?: string,
        public description?: string,
        public parentId?: any,
    ) {
    }
}
