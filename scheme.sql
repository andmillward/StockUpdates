create database Stocks2;use Stocks2;create table price_update (
	id int(8) unsigned not null auto_increment,
    price decimal(19,2) not null,
    transaction_time datetime not null,
    company_id int(8) unsigned not null,
    primary key (id),
    foreign key (company_id) references company(id)
);
create table company (
	id int(8) unsigned not null auto_increment,
    name varchar(500) not null,
    name varchar(500) not null,
    name varchar(20) not null,
    primary key (id)
);