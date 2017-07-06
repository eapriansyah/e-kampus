import { BaseEntity } from './../../shared';

export class PostStudent implements BaseEntity {
    constructor(
        public id?: any,
        public idPartyRole?: any,
        public idpoststudent?: string,
        public name?: string,
        public status?: number,
    ) {
    }
}
