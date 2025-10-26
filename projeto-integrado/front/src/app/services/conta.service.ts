import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

export interface Conta {
  id?: number;
  saldo?: number;
  version: number;
}

@Injectable({ providedIn: 'root' })
export class ContaService {
  private apiUrl = environment.apiUrl;
  private readonly baseUrl = `${this.apiUrl}/api/contas`;

  constructor(private readonly http: HttpClient) {}

  list(): Observable<Conta[]> {
    return this.http.get<Conta[]>(this.baseUrl);
  }
}
