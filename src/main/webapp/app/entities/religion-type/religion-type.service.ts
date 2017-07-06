import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';

import { ReligionType } from './religion-type.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class ReligionTypeService {

    private resourceUrl = 'api/religion-types';
    private resourceSearchUrl = 'api/_search/religion-types';

    constructor(private http: Http) { }

    create(religionType: ReligionType): Observable<ReligionType> {
        const copy = this.convert(religionType);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    update(religionType: ReligionType): Observable<ReligionType> {
        const copy = this.convert(religionType);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    find(id: any): Observable<ReligionType> {
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

	executeProcess(religionType: any): Observable<String> {
        const copy = this.convert(religionType);
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

    private convert(religionType: ReligionType): ReligionType {
        const copy: ReligionType = Object.assign({}, religionType);
        return copy;
    }
}
