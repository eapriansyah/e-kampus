import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';

import { Faculty } from './faculty.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class FacultyService {

    private resourceUrl = 'api/faculties';
    private resourceSearchUrl = 'api/_search/faculties';

    constructor(private http: Http) { }

    create(faculty: Faculty): Observable<Faculty> {
        const copy = this.convert(faculty);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    update(faculty: Faculty): Observable<Faculty> {
        const copy = this.convert(faculty);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    find(id: any): Observable<Faculty> {
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

	executeProcess(faculty: any): Observable<String> {
        const copy = this.convert(faculty);
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

    private convert(faculty: Faculty): Faculty {
        const copy: Faculty = Object.assign({}, faculty);
        return copy;
    }
}
