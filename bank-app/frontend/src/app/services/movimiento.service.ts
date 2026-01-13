import { Injectable } from '@angular/core';
import { ApiService } from './api.service';

@Injectable({ providedIn: 'root' })
export class MovimientoService {
  constructor(private api: ApiService) {}
  list() { return this.api.get('/movimientos'); }
  get(id: any) { return this.api.get('/movimientos/' + id); }
  create(data: any) { return this.api.post('/movimientos', data); }
  update(id: any, data: any) { return this.api.put('/movimientos/' + id, data); }
  delete(id: any) { return this.api.delete('/movimientos/' + id); }
}
