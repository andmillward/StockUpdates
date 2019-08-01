import {StockDataActions, StockDataActionTypes} from './StockDataActions';
import {List} from 'immutable';
import {InitialStockDataState, Stock, StockDataPayload, StockDataState} from './StateUtilities';

// Entry point for this features reductions (via module import). Decides which action is intended
export function StockDataReducer(state = InitialStockDataState, action: StockDataActions): StockDataState {
  switch (action.type) {
    case StockDataActionTypes.LoadStocks:
      return replaceData(state, action.payload);
    case StockDataActionTypes.UpdateStocks:
      return updateData(state, action.payload);
  }
  return state;
}

function replaceData(state: StockDataState, payload: StockDataPayload): StockDataState {
  return {
    data: List(payload.data.map(dat => new Stock(dat))),
    updateTime: payload.serverUpdatedTime
  };
}

function updateData(state: StockDataState, updatedPayload: StockDataPayload): StockDataState {
  const newStocks = [];
  state.data.forEach(currentStock => {
    const newStock = updatedPayload.data.find(updatedStock => updatedStock.company_id === currentStock.company_id);
    if (newStock) {
      newStocks.push(newStock);
    } else {
      newStocks.push(currentStock);
    }
  });
  return {
    data: List(newStocks.map(stock => new Stock(stock))),
    updateTime: updatedPayload.serverUpdatedTime
  };
}
