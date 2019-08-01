import {Component, OnDestroy, OnInit} from '@angular/core';
import {Subject} from 'rxjs';

import {StockDataService} from '../core/services/stock-data-service/stock-data.service';
import {takeUntil} from 'rxjs/operators';
import {List} from 'immutable';
import {Stock} from '../core/state/StateUtilities';

@Component({
  selector: 'app-stock-table',
  templateUrl: './stock-table.component.html',
  styleUrls: ['./stock-table.component.scss']
})
export class StockTableComponent implements OnInit, OnDestroy {

  public stocks: List<Stock> = List();
  public currentSortKey = 'id';
  public sortReversed = false;
  public unsubscribe = new Subject<any>();
  columns = [
    {name: 'stockSymbol', label: 'Stock'},
    {name: 'name', label: 'Name'},
    {name: 'price', label: 'Price'},
    {name: 'change', label: 'Change'},
    {name: 'percent', label: 'Percent'},
  ];

  constructor(private stockService: StockDataService) {
    this.stockService.subscribeToStockData().pipe(takeUntil(this.unsubscribe)).subscribe(data => {
      this.updateTableData(data);
    });
  }

  ngOnInit() { }

  ngOnDestroy() {
    this.unsubscribe.next();
    this.unsubscribe.complete();
  }

  // Service has returned data. Order correctly and replace local data
  private updateTableData(data) {
    this.stocks = this.orderBy(this.currentSortKey, data);
  }

  isSortedBy(sortKey: string) {
    return !this.sortReversed && this.currentSortKey === sortKey;
  }
  isReverseSortedBy(sortKey: string) {
    return this.sortReversed && this.currentSortKey === sortKey;
  }

  private orderToggleBy(key: string) {
    this.sortReversed = (key === this.currentSortKey) && !this.sortReversed;
    this.currentSortKey = key;
    this.stocks = this.orderBy(key, this.stocks);
  }

  // Uses immutableJS sortBy method
  private orderBy(key: any, stocks: List<Stock>) {
    const orderedStocks = stocks.sortBy((item) => item.get(key));
    return this.sortReversed ? orderedStocks.reverse() : orderedStocks;
  }
}
