import { Component, OnInit } from '@angular/core';
import { ClienteService } from '../../services/cliente.service';

@Component({
  selector: 'app-clientes',
  templateUrl: './clientes.component.html',
  styleUrls: ['./clientes.component.css']
})
export class ClientesComponent implements OnInit {
  clientes: any[] = [];
  filter = '';
  newCliente: any = { nombre: '', clienteId: '', estado: true };
  saved = false;
  constructor(private svc: ClienteService) {}
  ngOnInit(): void { this.load(); }
  load() { this.svc.list().subscribe((r: any) => this.clientes = r); }
  create(form: any) {
    if (!this.newCliente.nombre || !this.newCliente.clienteId) return;
    this.svc.create(this.newCliente).subscribe(() => { this.newCliente = { nombre: '', clienteId: '', estado: true }; this.load(); form.resetForm(); });
    this.saved = true;
    setTimeout(() => this.saved = false, 1200);
  }
  filtered() {
    if (!this.filter) return this.clientes;
    return this.clientes.filter(c => (c.nombre || '').toLowerCase().includes(this.filter.toLowerCase()) || (c.clienteId || '').toLowerCase().includes(this.filter.toLowerCase()));
  }
}
