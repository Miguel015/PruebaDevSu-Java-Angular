import { Injectable } from '@angular/core';
import { ApiService } from './api.service';

@Injectable({ providedIn: 'root' })
export class CuentaService {
  constructor(private api: ApiService) {}
  list() { return this.api.get('/cuentas'); }
  get(id: any) { return this.api.get('/cuentas/' + id); }
  create(data: any) { return this.api.post('/cuentas', data); }
  update(id: any, data: any) { return this.api.put('/cuentas/' + id, data); }
  delete(id: any) { return this.api.delete('/cuentas/' + id); }
}
