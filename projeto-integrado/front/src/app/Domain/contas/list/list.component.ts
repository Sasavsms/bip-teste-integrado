import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ListComponent, ListColumn } from '../../../components/list/list.component';
import { Conta, ContaService } from '../../../services/conta.service';
import {ReactiveFormsModule, FormBuilder, Validators, FormGroup, ValidationErrors} from '@angular/forms';
import {MatIconModule} from '@angular/material/icon';
import {CreateTransferenciaDto, TransferenciaService} from '../../../services/transferencia.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-contas-list',
  standalone: true,
  imports: [CommonModule, ListComponent, ReactiveFormsModule, MatIconModule],
  templateUrl: './list.component.html',
  styleUrls: ['./list.component.scss']
})
export class ContasListComponent implements OnInit {
  private readonly service = inject(ContaService);
  private readonly trasnferenciaService = inject(TransferenciaService);
  private readonly fb = inject(FormBuilder);

  columns: ListColumn[] = [
    { key: 'id', label: 'ID' },
    { key: 'saldo', label: 'Saldo' },
  ];

  data: Conta[] = [];
  loading = false;
  error: string | null = null;

  form = this.fb.group({
    origem: [null as number | null, [Validators.required]],
    destino: [null as number | null, [Validators.required]],
    valor: [null as number | null, [Validators.required, Validators.min(0.01)]],
  }, { validators: this.diferenteOrigemDestino });

  ngOnInit(): void {
    this.load();
  }

  onTransferir(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    const dto: CreateTransferenciaDto = {
      origem: this.form.get('origem')?.value,
      destino: this.form.get('destino')?.value,
      valor: this.form.get('valor')?.value,
    }
    this.trasnferenciaService.create(dto).subscribe((res) => {
      Swal.fire({
        icon: 'success',
        title: 'Sucesso!',
        text: res || 'Transferência realizada com sucesso.',
        confirmButtonColor: '#3085d6'
      });
      this.load();
      this.form.reset();
    })
  }

  load(): void {
    this.loading = true;
    this.error = null;
    this.service.list().subscribe({
      next: (items) => {
        this.data = items ?? [];
        this.loading = false;
      },
      error: (err) => {
        console.error(err);
        this.error = 'Falha ao carregar contas.';
        this.loading = false;
      }
    });
  }


  diferenteOrigemDestino(form: FormGroup): ValidationErrors | null {
    const origem = form.get('origem')?.value;
    const destino = form.get('destino')?.value;
    return origem && destino && origem === destino
      ? { mesmaConta: true }
      : null;
  }
}
