import {List, Record} from 'immutable';
import {createFeatureSelector, createSelector} from '@ngrx/store';

/*
  Gives this feature it's own data store so the entire application data
  does not need to be passed around
 */
export const STOCK_FEATURE_NAME = 'STOCK_DATA_FEATURE';
export interface StockDataState {
  data: List<Stock>;
  updateTime: string;
}

// Names the store for this feature
export const StockFeatureSelector = createFeatureSelector<StockDataState>(STOCK_FEATURE_NAME);

// Keys for selecting the components in the store for this feature
export const StockDataSelector = createSelector(StockFeatureSelector, state => {
  return state.data;
});
export const StockUpdateTimeSelector = createSelector(StockFeatureSelector, state => {
  return state.updateTime;
});

export const InitialStockDataState: StockDataState = {
  data: List(),
  updateTime: null,
};

const StockRecord = Record({
  id: 0,
  name: '',
  stockSymbol: '',
  website: '',
  price: 0,
  change: 0,
  percent: 0,
  company_id: 0
});

export interface IStock {
  id: number;
  name: string;
  stockSymbol: string;
  website: string;
  price: number;
  change: number;
  percent: number;
  company_id: number;
}

/*
  A combination of Stock and StockRecord are required to give a Stock
  both typing and immutability.
 */
export class Stock extends StockRecord implements IStock {
  change: number;
  id: number;
  name: string;
  percent: number;
  price: number;
  stockSymbol: string;
  website: string;
  constructor(stock: IStock) {
    super(stock);
  }
}

// Cast data retrieved from service to this type
export interface StockDataPayload {
  data: IStock[];
  serverUpdatedTime: string;
}
