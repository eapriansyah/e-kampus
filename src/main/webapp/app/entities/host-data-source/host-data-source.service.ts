import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';

import { HostDataSource } from './host-data-source.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class HostDataSourceService {

    private resourceUrl = 'api/host-data-sources';
    private resourceSearchUrl = 'api/_search/host-data-sources';

    constructor(private http: Http) { }

    create(hostDataSource: HostDataSource): Observable<HostDataSource> {
        const copy = this.convert(hostDataSource);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    update(hostDataSource: HostDataSource): Observable<HostDataSource> {
        const copy = this.convert(hostDataSource);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    find(id: any): Observable<HostDataSource> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            return res.json();
        });
    }

    query(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceUrl, options)
            .map((res: Response) => this.convertResponse(res));
    }


    delete(id?: any): Observable<Response> {
        return this.http.delete(`${this.resourceUrl}/${id}`);
    }

	executeProcess(hostDataSource: any): Observable<String> {
        const copy = this.convert(hostDataSource);
        return this.http.post(this.resourceUrl + '/execute', copy).map((res: Response) => {
            return res.json();
        });
    }

    search(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceSearchUrl, options)
            .map((res: any) => this.convertResponse(res));
    }

    private convertResponse(res: Response): ResponseWrapper {
        const jsonResponse = res.json();
        return new ResponseWrapper(res.headers, jsonResponse, res.status);
    }

    private convert(hostDataSource: HostDataSource): HostDataSource {
        const copy: HostDataSource = Object.assign({}, hostDataSource);
        return copy;
    }
}
