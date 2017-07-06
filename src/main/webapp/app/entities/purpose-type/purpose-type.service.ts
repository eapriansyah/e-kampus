import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';

import { PurposeType } from './purpose-type.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class PurposeTypeService {

    private resourceUrl = 'api/purpose-types';
    private resourceSearchUrl = 'api/_search/purpose-types';

    constructor(private http: Http) { }

    create(purposeType: PurposeType): Observable<PurposeType> {
        const copy = this.convert(purposeType);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    update(purposeType: PurposeType): Observable<PurposeType> {
        const copy = this.convert(purposeType);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    find(id: any): Observable<PurposeType> {
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

	executeProcess(purposeType: any): Observable<String> {
        const copy = this.convert(purposeType);
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

    private convert(purposeType: PurposeType): PurposeType {
        const copy: PurposeType = Object.assign({}, purposeType);
        return copy;
    }
}
