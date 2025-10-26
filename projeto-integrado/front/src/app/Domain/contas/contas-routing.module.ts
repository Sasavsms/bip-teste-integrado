import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ContasListComponent } from './list/list.component';

const routes: Routes = [
  { path: 'list', component: ContasListComponent },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ContasRoutingModule {}
