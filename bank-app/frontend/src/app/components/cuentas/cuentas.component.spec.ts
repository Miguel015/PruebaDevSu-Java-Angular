import { CuentasComponent } from './cuentas.component';
import { of } from 'rxjs';

describe('CuentasComponent', () => {
  it('debe cargar lista de cuentas', () => {
    const svc: any = { list: () => of([{ id:1, numeroCuenta:'123' }]) };
    const comp = new CuentasComponent(svc);
    comp.ngOnInit();
    expect(comp.cuentas.length).toBe(1);
  });
});
