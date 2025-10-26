import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TransferenciasRoutingModule } from './transferencias-routing.module';
import { TransferenciasListComponent } from './list/list.component';

@NgModule({
  imports: [CommonModule, TransferenciasRoutingModule, TransferenciasListComponent],
})
export class TransferenciasModule {}
