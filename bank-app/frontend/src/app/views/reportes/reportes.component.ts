import { Component } from '@angular/core';
import { ApiService } from '../../services/api.service';

@Component({
  selector: 'app-reportes',
  templateUrl: './reportes.component.html',
  styleUrls: ['./reportes.component.css']
})
export class ReportesComponent {
  pdfBase64: string | null = null;
  constructor(private api: ApiService) {}
  generar(clienteId: string, inicio: string, fin: string) {
    this.api.get(`/reportes?clienteId=${clienteId}&fechaInicio=${encodeURIComponent(inicio)}&fechaFin=${encodeURIComponent(fin)}`)
      .subscribe((r: any) => { this.pdfBase64 = r.pdfBase64; });
  }
  descargar() {
    if (!this.pdfBase64) return;
    const link = document.createElement('a');
    link.href = 'data:application/pdf;base64,' + this.pdfBase64;
    link.download = 'reporte.pdf';
    link.click();
  }
}
