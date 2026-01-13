import { ClienteService } from './cliente.service';

describe('ClienteService', () => {
  it('llama ApiService con ruta correcta para list', () => {
    const api: any = { get: jest.fn() };
    const svc = new ClienteService(api);
    svc.list();
    expect(api.get).toHaveBeenCalledWith('/clientes');
  });
});
