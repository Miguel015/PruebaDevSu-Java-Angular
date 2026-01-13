import { Injectable } from '@angular/core';
import { ApiService } from './api.service';

@Injectable({ providedIn: 'root' })
export class ClienteService {
  constructor(private api: ApiService) {}
  list() { return this.api.get('/clientes'); }
  get(id: any) { return this.api.get('/clientes/' + id); }
  create(data: any) { return this.api.post('/clientes', data); }
  update(id: any, data: any) { return this.api.put('/clientes/' + id, data); }
  delete(id: any) { return this.api.delete('/clientes/' + id); }
}
