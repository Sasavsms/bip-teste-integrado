import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ContasRoutingModule } from './contas-routing.module';
import { ContasListComponent } from './list/list.component';

@NgModule({
  imports: [CommonModule, ContasRoutingModule, ContasListComponent],
})
export class ContasModule {}
