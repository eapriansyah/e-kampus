import { BaseEntity } from './../../shared';

export class ElectronicAddress implements BaseEntity {
    constructor(
        public id?: any,
        public idContact?: any,
        public address?: string,
    ) {
    }
}
