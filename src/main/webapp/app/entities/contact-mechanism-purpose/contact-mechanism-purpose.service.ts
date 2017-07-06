import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils } from 'ng-jhipster';

import { ContactMechanismPurpose } from './contact-mechanism-purpose.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class ContactMechanismPurposeService {

    private resourceUrl = 'api/contact-mechanism-purposes';
    private resourceSearchUrl = 'api/_search/contact-mechanism-purposes';

    constructor(private http: Http, private dateUtils: JhiDateUtils) { }

    create(contactMechanismPurpose: ContactMechanismPurpose): Observable<ContactMechanismPurpose> {
        const copy = this.convert(contactMechanismPurpose);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    update(contactMechanismPurpose: ContactMechanismPurpose): Observable<ContactMechanismPurpose> {
        const copy = this.convert(contactMechanismPurpose);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    find(id: any): Observable<ContactMechanismPurpose> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
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

	executeProcess(contactMechanismPurpose: any): Observable<String> {
        const copy = this.convert(contactMechanismPurpose);
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
        for (let i = 0; i < jsonResponse.length; i++) {
            this.convertItemFromServer(jsonResponse[i]);
        }
        return new ResponseWrapper(res.headers, jsonResponse, res.status);
    }

    private convertItemFromServer(entity: any) {
        entity.dateFrom = this.dateUtils
            .convertDateTimeFromServer(entity.dateFrom);
        entity.dateThru = this.dateUtils
            .convertDateTimeFromServer(entity.dateThru);
    }

    private convert(contactMechanismPurpose: ContactMechanismPurpose): ContactMechanismPurpose {
        const copy: ContactMechanismPurpose = Object.assign({}, contactMechanismPurpose);

        copy.dateFrom = this.dateUtils.toDate(contactMechanismPurpose.dateFrom);

        copy.dateThru = this.dateUtils.toDate(contactMechanismPurpose.dateThru);
        return copy;
    }
}
