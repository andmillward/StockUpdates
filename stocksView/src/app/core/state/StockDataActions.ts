import {Action} from '@ngrx/store';
import {StockDataPayload} from './StateUtilities';

// Defines the action as having gotten all data or just the updates from the service
export enum StockDataActionTypes {
  LoadStocks = 'LoadStocks',
  UpdateStocks = 'UpdateStocks'
}

export class LoadStocks implements Action {
  readonly type  = StockDataActionTypes.LoadStocks;
  constructor(public payload: StockDataPayload) {}
}

export class UpdateStocks implements Action {
  readonly type  = StockDataActionTypes.UpdateStocks;
  constructor(public payload: StockDataPayload) {}
}


export type StockDataActions = LoadStocks | UpdateStocks;
