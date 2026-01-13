import { ClientesComponent } from './clientes.component';
import { of } from 'rxjs';

describe('ClientesComponent', () => {
  it('debe cargar lista de clientes', () => {
    const svc: any = { list: () => of([{ id:1, nombre:'Juan' }]) };
    const comp = new ClientesComponent(svc);
    comp.ngOnInit();
    expect(comp.clientes.length).toBe(1);
  });
});
