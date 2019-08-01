import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { StockTableComponent } from './stock-table/stock-table.component';

const routes: Routes = [
  { path: '', component: StockTableComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
