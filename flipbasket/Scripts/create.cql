/*
	The objective is as follows - 
	1. Handle eventual consistency in the order page
	2. Create a new admin page "Search", given an order number show full order+user details
	3. Create a new management dashboard page to show the revenue by year/month by city
*/

DROP KEYSPACE flipbasket;

CREATE KEYSPACE flipbasket WITH replication = {'class':'SimpleStrategy', 'replication_factor' : 1};

CREATE KEYSPACE flipbasket WITH replication = {'class':'NetworkTopologyStrategy', 'dc1' : 1 };

drop table if exists flipbasket.products;

create table flipbasket.products (
	id uuid,
	type text,
	short_desc text,
	long_desc text,
	price double,
	image blob,
	PRIMARY KEY (id)
);


CREATE TABLE flipbasket.users (
    user_id text,
    password text,
	first_name text,
	last_name text,
	address_1 text,
	address_2 text,
	city text,
	phone text,
	created_on timestamp,
	modified_on timestamp,
    PRIMARY KEY ((user_id))
);


--Orders for any given user partitioned by year; including "date" in PK to support any non uuid based order num
--where there may be duplicates (just safety measure)
CREATE TABLE flipbasket.orders (
	user_id text,
	year int,
	month int,
	order_num timeuuid,
	date int,
	status int,
	amount double,
	tax double,
	details text,
	created_on timestamp,
	PRIMARY KEY ((user_id, year), order_num, date)
) WITH CLUSTERING ORDER BY (order_num DESC, date DESC);


--Pull a user record given an order number
CREATE TABLE flipbasket.orders_idx1 (
	order_num timeuuid,
	user_id text,
	year int,
	date int,
	PRIMARY KEY ((order_num))
);


--what is my revenue from a city for a given month? Can you analyze this table structure wrt V3.volume param?
CREATE TABLE flipbasket.orders_idx2 (
	city text,
	year int,
	month int,
	user_id text,
	order_num timeuuid,
	amount double,
	PRIMARY KEY ((city, year, month), user_id, order_num)
);

