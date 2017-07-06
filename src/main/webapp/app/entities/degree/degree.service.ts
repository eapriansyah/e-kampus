import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';

import { Degree } from './degree.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class DegreeService {

    private resourceUrl = 'api/degrees';
    private resourceSearchUrl = 'api/_search/degrees';

    constructor(private http: Http) { }

    create(degree: Degree): Observable<Degree> {
        const copy = this.convert(degree);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    update(degree: Degree): Observable<Degree> {
        const copy = this.convert(degree);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    find(id: any): Observable<Degree> {
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

	executeProcess(degree: any): Observable<String> {
        const copy = this.convert(degree);
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

    private convert(degree: Degree): Degree {
        const copy: Degree = Object.assign({}, degree);
        return copy;
    }
}
