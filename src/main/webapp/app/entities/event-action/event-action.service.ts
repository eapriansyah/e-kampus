import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';

import { EventAction } from './event-action.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class EventActionService {

    private resourceUrl = 'api/event-actions';
    private resourceSearchUrl = 'api/_search/event-actions';

    constructor(private http: Http) { }

    create(eventAction: EventAction): Observable<EventAction> {
        const copy = this.convert(eventAction);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    update(eventAction: EventAction): Observable<EventAction> {
        const copy = this.convert(eventAction);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    find(id: any): Observable<EventAction> {
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

	executeProcess(eventAction: any): Observable<String> {
        const copy = this.convert(eventAction);
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

    private convert(eventAction: EventAction): EventAction {
        const copy: EventAction = Object.assign({}, eventAction);
        return copy;
    }
}
