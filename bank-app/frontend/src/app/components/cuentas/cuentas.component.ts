import { Component, OnInit } from '@angular/core';
import { CuentaService } from '../../services/cuenta.service';
import { ClienteService } from '../../services/cliente.service';

@Component({
  selector: 'app-cuentas',
  templateUrl: './cuentas.component.html',
  styleUrls: ['./cuentas.component.css']
})
export class CuentasComponent implements OnInit {
  cuentas: any[] = [];
  clients: any[] = [];
  filter = '';
  newCuenta: any = { numeroCuenta: '', tipoCuenta: 'Ahorro', saldoInicial: 0, clienteId: null };
  saved = false;
  err = '';
  constructor(private svc: CuentaService, private clientSvc: ClienteService) {}
  ngOnInit(): void { this.load(); this.loadClients(); }
  load() { this.svc.list().subscribe((r: any) => this.cuentas = r); }
  loadClients() { this.clientSvc.list().subscribe((r: any) => this.clients = r); }
  create(form: any) {
    this.err = '';
    if (!this.newCuenta.numeroCuenta || !this.newCuenta.clienteId) { this.err = 'Número y cliente obligatorios'; return; }
    this.svc.create(this.newCuenta).subscribe(() => {
      this.newCuenta = { numeroCuenta: '', tipoCuenta: 'Ahorro', saldoInicial: 0, clienteId: null };
      this.load(); form.resetForm();
      this.saved = true; setTimeout(() => this.saved = false, 1200);
    }, (e: any) => {
      this.err = e?.error?.error || e?.error?.message || 'Error al crear cuenta';
    });
  }
  filtered() {
    if (!this.filter) return this.cuentas;
    return this.cuentas.filter(c => (c.numeroCuenta || '').toLowerCase().includes(this.filter.toLowerCase()));
  }
}
