# Stock Project

The aim of the web stock project is a proof of concept to provide stock summaries by company in real time to users. The client consists of the information displayed in a table format that is sortable and has links to the companies respective websites. This is updated to current values without having to reload the page. 

The backed is accessed via RESTful services. It retrieves data from a mock service and builds a database of the history. In memory, it keeps track of the last update times for each company so that the clients can request lean responses. When the client receives the new information it creates a new NGRX state and triggers Angular’s change detection. The styles are updated to compliment the values.

## Angular Front End

**Stock Table Component**
The stock table component is the main component and handles the display of stock summary information. It retrieves its information from the stock data service. The component orders the data by the users sort selection.

**Stock Data Service**
Accesses the back end REST endpoints and make the data available by placing it in the data store which will automatically update relevant components.

**NGRX Stock Data Feature**
Holds the current state of the data for asynchronous access. Windows it as a part of the stocks feature. From here the data can be replaced or updated. These actions are reduced with the current data store to create an new version, up to date with the latest information from the back end.

## Java Spring Back End

**Repositories**
There is a repository for each entity that extend the built in JPA repository from the Spring Framework.

**Entities**
The Price and Company entities hold records from databases. Each company has a history of Price updates as a list.

**Data Transfer Objects**
StockSummaryResponse holds information about when the data was accessed and the data itself as a list of StockSummarys.

**Controllers**
Handles requests on provided endpoints to clients by gathering information via the services. This can be all the data or be based on the last up-to-date time of the client.

**Services**
The StockService fetches data from StockDataService on an interval, in our case that is MockStockDataService which mocks the data that would be returned by a 3rd party or external service. Once a request is received from the client via the controller, the service will return a list of stock summaries by company from the repositories. The list can either be all the summaries for every company, or just the records updated since the client's last request. 

## MySQL Database
Standard MySQL server with 2 tables. One for the list of companies in the stock market being tracked. Another for the many updates to a stock's value. One company to its many price updates.
