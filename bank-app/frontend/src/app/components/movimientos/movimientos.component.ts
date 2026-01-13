import { Component, OnInit } from '@angular/core';
import { MovimientoService } from '../../services/movimiento.service';
import { CuentaService } from '../../services/cuenta.service';

@Component({
  selector: 'app-movimientos',
  templateUrl: './movimientos.component.html',
  styleUrls: ['./movimientos.component.css']
})
export class MovimientosComponent implements OnInit {
  movimientos: any[] = [];
  cuentas: any[] = [];
  newMov: any = { tipoMovimiento: 'CREDITO', valor: 0, cuentaId: null };
  saved = false;
  err = '';
  constructor(private svc: MovimientoService, private cuentaSvc: CuentaService) {}
  ngOnInit(): void { this.load(); this.loadCuentas(); }
  load() { this.svc.list().subscribe((r: any) => this.movimientos = r); }
  loadCuentas() { this.cuentaSvc.list().subscribe((r: any) => this.cuentas = r); }
  create(form: any) {
    this.err = '';
    if (!this.newMov.tipoMovimiento || !this.newMov.valor || !this.newMov.cuentaId) { this.err = 'Todos los campos son obligatorios'; return; }
    this.svc.create(this.newMov).subscribe(() => {
      this.newMov = { tipoMovimiento: 'CREDITO', valor: 0, cuentaId: null };
      this.load(); form.resetForm();
      this.saved = true; setTimeout(() => this.saved = false, 1200);
    }, (e: any) => { this.err = e?.error?.error || e?.error?.message || 'Error al crear movimiento'; });
  }
}
