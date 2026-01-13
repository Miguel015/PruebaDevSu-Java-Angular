import { Component } from '@angular/core';

@Component({
  selector: 'app-root',
  template: `
    <h1>Bank App</h1>
    <nav>
      <a href="#/clientes">Clientes</a> |
      <a href="#/cuentas">Cuentas</a> |
      <a href="#/movimientos">Movimientos</a> |
      <a href="#/reportes">Reportes</a>
    </nav>
    <div id="view">Seleccione una vista</div>
  `
})
export class AppComponent { }
