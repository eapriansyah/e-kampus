import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';

import { StudentEvent } from './student-event.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class StudentEventService {

    private resourceUrl = 'api/student-events';
    private resourceSearchUrl = 'api/_search/student-events';

    constructor(private http: Http) { }

    create(studentEvent: StudentEvent): Observable<StudentEvent> {
        const copy = this.convert(studentEvent);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    update(studentEvent: StudentEvent): Observable<StudentEvent> {
        const copy = this.convert(studentEvent);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    find(id: any): Observable<StudentEvent> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            return res.json();
        });
    }

    query(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceUrl, options)
            .map((res: Response) => this.convertResponse(res));
    }

    queryFilterBy(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceUrl + '/filterBy', options)
            .map((res: Response) => this.convertResponse(res));
    }

    delete(id?: any): Observable<Response> {
        return this.http.delete(`${this.resourceUrl}/${id}`);
    }

	executeProcess(studentEvent: any): Observable<String> {
        const copy = this.convert(studentEvent);
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

    private convert(studentEvent: StudentEvent): StudentEvent {
        const copy: StudentEvent = Object.assign({}, studentEvent);
        return copy;
    }
}
