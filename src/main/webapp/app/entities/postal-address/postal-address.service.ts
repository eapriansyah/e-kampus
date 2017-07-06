import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';

import { PostalAddress } from './postal-address.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class PostalAddressService {

    private resourceUrl = 'api/postal-addresses';
    private resourceSearchUrl = 'api/_search/postal-addresses';

    constructor(private http: Http) { }

    create(postalAddress: PostalAddress): Observable<PostalAddress> {
        const copy = this.convert(postalAddress);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    update(postalAddress: PostalAddress): Observable<PostalAddress> {
        const copy = this.convert(postalAddress);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    find(id: any): Observable<PostalAddress> {
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

	executeProcess(postalAddress: any): Observable<String> {
        const copy = this.convert(postalAddress);
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

    private convert(postalAddress: PostalAddress): PostalAddress {
        const copy: PostalAddress = Object.assign({}, postalAddress);
        return copy;
    }
}
