import { BaseEntity } from './../../shared';

export class PreStudent implements BaseEntity {
    constructor(
        public id?: any,
        public idPartyRole?: any,
        public idPreStudent?: string,
        public name?: string,
        public yearOf?: number,
        public status?: number,
        public prodyId?: any,
        public studyPathId?: any,
    ) {
    }
}
