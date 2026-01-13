import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { AppComponent } from './app.component';
import { ClientesComponent } from './components/clientes/clientes.component';
import { CuentasComponent } from './components/cuentas/cuentas.component';
import { MovimientosComponent } from './components/movimientos/movimientos.component';
import { ReportesComponent } from './views/reportes/reportes.component';

@NgModule({
  declarations: [AppComponent, ClientesComponent, CuentasComponent, MovimientosComponent, ReportesComponent],
  imports: [BrowserModule, HttpClientModule, FormsModule],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
