import { Component, Input } from '@angular/core';
import {Router} from '@angular/router';
import { MatIconModule } from '@angular/material/icon';
import {FormatTimePipe} from '../../pipes/format-time.pipe';
import {FormatCurrencyPipe} from '../../pipes/format-currency.pipe';

export type ListColumn = { key: string; label: string };

@Component({
  selector: 'app-list',
  standalone: true,
  imports: [MatIconModule, FormatTimePipe, FormatCurrencyPipe],
  template: `
    <div class="table-responsive">
      <table class="table table-striped table-bordered align-middle mb-0">
        <thead class="table-light">
          <tr>
            @for (col of columns; track col.key) {
              <th scope="col">{{ col.label }}</th>
            }
            @if (hasActions) {
              <th scope="col">Ações</th>
            }
          </tr>
        </thead>
        <tbody>
          @if (!data || data.length === 0) {
            <tr>
              <td [attr.colspan]="columns.length || 1" class="text-center text-muted py-4">
                Nenhum registro encontrado
              </td>
            </tr>
          } @else {
            @for (row of data; track $index) {
              <tr>
                @for (col of columns; track col.key) {
                  <td>@if (col.key === 'dataHora') {
                    {{ row?.[col.key] | formatTime }}
                  } @else if (col.key === 'valor' || col.key === 'saldo') {
                    {{ row?.[col.key] | formatCurrency }}
                  } @else {
                    {{ row?.[col.key] }}
                  }</td>
                }
                @if (hasActions) {
                  <td class="text-center">
                    <button
                      class="btn btn-sm btn-outline-primary"
                      (click)="navigate(row)"
                    >
                      <mat-icon style="height: 20px !important">visibility</mat-icon>
                    </button>
                  </td>
                }
              </tr>
            }
          }
        </tbody>
      </table>
    </div>
  `,
})
export class ListComponent {
  @Input({ required: true }) columns: ListColumn[] = [];
  @Input({ required: true }) data: Record<string, any>[] = [];
  @Input() hasActions = false;

  constructor(private router: Router) {}

  navigate(row: Record<string, any>) {
    console.log(row);
    this.router.navigate(['/transferencias', 'list', row['id']]);
  }
}
