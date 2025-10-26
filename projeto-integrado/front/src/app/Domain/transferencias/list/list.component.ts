import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { ListComponent, ListColumn } from '../../../components/list/list.component';
import { Transferencia, TransferenciaService } from '../../../services/transferencia.service';
import {MatIconModule} from '@angular/material/icon';

@Component({
  selector: 'app-transferencias-list',
  standalone: true,
  imports: [CommonModule, ListComponent, MatIconModule],
  templateUrl: './list.component.html',
  styleUrls: ['./list.component.scss']
})
export class TransferenciasListComponent implements OnInit {
  private readonly service = inject(TransferenciaService);
  private readonly route = inject(ActivatedRoute);

  columns: ListColumn[] = [];

  data: Transferencia[] = [];
  loading = false;
  error: string | null = null;
  contaId: number | null = null;

  ngOnInit(): void {
    const idParam = this.route.snapshot.paramMap.get('id');
    const id = idParam ? Number(idParam) : NaN;
    if (!isNaN(id)) {
      this.contaId = id;
      this.load(id);
    } else {
      this.error = 'ID inválido na rota.';
    }
  }

  load(id: number): void {
    this.loading = true;
    this.error = null;

    this.service.listByConta(id).subscribe({
      next: (items: Transferencia[]) => {
      console.log(items);
        const mapped = (items ?? []).filter(t => t.contaOrigem.id === id).map(t => ({
          ...t,
          contaOrigemId: t.contaOrigem?.id ?? null,
          contaDestinoId: t.contaDestino?.id ?? null,
          contaParceiraId: t.contaDestino?.id,
          contaParceiraLabel: t.tipo === 'ENTRADA'
            ? 'Conta Origem'
            : 'Conta Destino'
        })) .sort((a, b) => b.id - a?.id);

        const firstTipo = mapped[0]?.tipo;
        this.columns = [
          { key: 'id', label: 'ID' },
          {
            key: 'contaParceiraId',
            label: firstTipo === 'ENTRADA' ? 'Conta Origem' : 'Conta Destino'
          },
          { key: 'valor', label: 'Valor' },
          { key: 'dataHora', label: 'Data/Hora' },
          { key: 'tipo', label: 'Tipo' },
        ];
        this.data = mapped;
        this.loading = false;
      },
      error: (err: any): void => {
        console.error(err);
        this.error = 'Falha ao carregar transferências.';
        this.loading = false;
      }
    });
  }

}
