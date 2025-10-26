import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import {environment} from '../../environments/environment';
import {Conta} from './conta.service';

export interface CreateTransferenciaDto {
  origem: number;
  destino: number;
  valor: number;
}

export interface Transferencia {
  id: number;
  contaOrigem: Conta;
  contaDestino: Conta;
  valor: number;
  dataHora?: string;
  tipo: string
}

@Injectable({ providedIn: 'root' })
export class TransferenciaService {
  private apiUrl = environment.apiUrl;
  private readonly baseUrl = `${this.apiUrl}/api/transferencias`;

  constructor(private readonly http: HttpClient) {}

  create(dto: CreateTransferenciaDto): Observable<string> {
    return this.http.post(this.baseUrl, dto, { responseType: 'text' });
  }

  listByConta(numeroConta: number): Observable<Transferencia[]> {
    return this.http.get<Transferencia[]>(`${this.baseUrl}/${numeroConta}`);
  }
}
