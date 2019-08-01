import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { StockTableComponent } from './stock-table/stock-table.component';
import { HeaderComponent } from './header/header.component';
import {StoreModule} from '@ngrx/store';
import {AppCoreModule} from './core/app-core.module';

@NgModule({
  declarations: [
    AppComponent,
    StockTableComponent,
    HeaderComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    StoreModule.forRoot({}),
    AppCoreModule,
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
