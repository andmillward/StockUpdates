import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Store} from '@ngrx/store';
import {Observable, timer} from 'rxjs';
import {LoadStocks, UpdateStocks} from '../../state/StockDataActions';
import {List} from 'immutable';
import {first, switchMap} from 'rxjs/operators';
import {Stock, StockDataPayload, StockDataSelector, StockDataState, StockUpdateTimeSelector} from '../../state/StateUtilities';


@Injectable({
  providedIn: 'root'
})
export class StockDataService {

  // Initialize feature store with all data and start asking for updates
  constructor(private httpClient: HttpClient, private stockStore: Store<StockDataState>) {
    this.fetchStockData().subscribe(data => {
      this.stockStore.next(new LoadStocks(data));
    }, () => console.log('Failed to load stocks'));
    this.startUpdates();
  }

  // Need all records as a StockDataPayload
  private fetchStockData() {
    return this.httpClient.get<StockDataPayload>('/api/stock-summaries');
  }

  // Need summaries updated since last request
  private fetchStockDataUpdates(): Observable<StockDataPayload> {
    return this.stockStore.select(StockUpdateTimeSelector).pipe(first(), switchMap(time => {
      let url = '/api/stock-summaries';
      url = time ? url + '?updateTime=' + time : url;
      return this.httpClient.get<StockDataPayload>(url);
    }));
  }

  public subscribeToStockData(): Observable<List<Stock>> {
    return this.stockStore.select(StockDataSelector);
  }

  // Need updates put in the data store on a regular interval from endpoint
  private startUpdates() {
    timer(10000, 10000).subscribe(() => {
      this.fetchStockDataUpdates().subscribe(data => {
        this.stockStore.next(new UpdateStocks(data));
      },
        () => console.log('Failed to update stocks'));
    });
  }
}
