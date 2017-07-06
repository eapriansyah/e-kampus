import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';

import { Lecture } from './lecture.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class LectureService {

    private resourceUrl = 'api/lectures';
    private resourceSearchUrl = 'api/_search/lectures';

    constructor(private http: Http) { }

    create(lecture: Lecture): Observable<Lecture> {
        const copy = this.convert(lecture);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    update(lecture: Lecture): Observable<Lecture> {
        const copy = this.convert(lecture);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    find(id: any): Observable<Lecture> {
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

	executeProcess(lecture: any): Observable<String> {
        const copy = this.convert(lecture);
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

    private convert(lecture: Lecture): Lecture {
        const copy: Lecture = Object.assign({}, lecture);
        return copy;
    }
}
