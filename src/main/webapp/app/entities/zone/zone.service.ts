import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';

import { Zone } from './zone.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class ZoneService {

    private resourceUrl = 'api/zones';
    private resourceSearchUrl = 'api/_search/zones';

    constructor(private http: Http) { }

    create(zone: Zone): Observable<Zone> {
        const copy = this.convert(zone);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    update(zone: Zone): Observable<Zone> {
        const copy = this.convert(zone);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    find(id: any): Observable<Zone> {
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

	executeProcess(zone: any): Observable<String> {
        const copy = this.convert(zone);
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

    private convert(zone: Zone): Zone {
        const copy: Zone = Object.assign({}, zone);
        return copy;
    }
}
