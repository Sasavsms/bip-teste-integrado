import { Routes } from '@angular/router';

export const routes: Routes = [
  { path: '', pathMatch: 'full', redirectTo: 'contas/list' },
  {
    path: 'contas',
    loadChildren: () => import('./Domain/contas/contas.module').then(m => m.ContasModule)
  },
  {
    path: 'transferencias',
    loadChildren: () => import('./Domain/transferencias/transferencias.module').then(m => m.TransferenciasModule)
  },
  { path: '**', redirectTo: 'contas/list' }
];
