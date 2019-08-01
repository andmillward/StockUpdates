import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {StockDataService} from './services/stock-data-service/stock-data.service';
import {StoreModule} from '@ngrx/store';
import {STOCK_FEATURE_NAME} from './state/StateUtilities';
import {StockDataReducer} from './state/StockDataReducers';

@NgModule({
  declarations: [],
  imports: [
    CommonModule,
    StoreModule.forFeature(STOCK_FEATURE_NAME, StockDataReducer)
  ],
  providers: [
    StockDataService
  ],
})
export class AppCoreModule { }
