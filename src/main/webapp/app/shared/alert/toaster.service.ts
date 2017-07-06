import {Injectable} from '@angular/core';
import {BehaviorSubject} from 'rxjs/BehaviorSubject';
import {Message} from 'primeng/primeng';

@Injectable()
export class ToasterService {
    public toasterStatus: BehaviorSubject<Message> = new BehaviorSubject<Message>(null);

    // type: success, info, warn, error
    showToaster(type: string, header: string, content: string) {
        let objToaster: Message = { severity: type, summary: header, detail: content };
        this.toasterStatus.next(objToaster);
    }

    hideToaster() {
        this.toasterStatus.next(null);
    }
}
