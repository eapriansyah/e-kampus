import { BaseEntity } from './../../shared';

export class HostDataSource implements BaseEntity {
    constructor(
        public id?: any,
        public idHostDataSource?: any,
        public tabelMahasiswa?: string,
        public tabelMataKuliah?: string,
        public tabelDosen?: string,
        public tabelKelas?: string,
        public tabelNilai?: string,
        public className?: string,
        public userName?: string,
        public password?: string,
        public jdbcUrl?: string,
        public connectionTimeOut?: number,
        public minimumPoolSize?: number,
        public maximumPoolSize?: number,
        public isActive?: string,
        public prodyId?: any,
        public studyPathId?: any,
    ) {
    }
}
