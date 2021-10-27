-- User Roles Table
-- drop table user_roles;
create table user_roles(
	roleid int primary key,
	role varchar(10)
);

-- Users Table
-- drop table users cascade;
create table users(
	userid int primary key generated always as identity,
	username varchar(50) unique not null,
	password varchar(64) not null,
	firstname varchar(100) not null,
	lastname varchar(100) not null,
	email varchar(150) unique not null,
	roleid int,
	foreign key (roleid) references user_roles(roleid)
); 

-- Request Type Table
-- drop table request_type;
create table request_type(
	typeid int primary key,
	type varchar(25)
);

-- Request Status Table
-- drop table request_status;
create table request_status(
	statusid int primary key,
	status varchar(10)
);

-- Requests Table
--drop table requests;
create table requests(
	requestid int primary key generated always as identity,
	amount double precision,
	submitted timestamp default cast(current_timestamp as timestamp),
	resolved timestamp,
	description varchar(250),
	receipt bytea,
	authorid int,
	resolverid int,
	statusid int,
	typeid int,
	foreign key (statusid) references request_status(statusid),
	foreign key (typeid) references request_type(typeid)
);

-- Junction Table
--drop table user_request_xref;
create table user_request_xref(
	username varchar(50) references users(username) on delete cascade,
	requestid int references requests(requestid) on delete cascade,
	primary key(username, requestid)
);
-----------------------------------------------------------------------------------------------------	
-- Functions
-- inserts into users and user_roles
create or replace function insert_user(username varchar(50), password varchar(64), 
	firstname varchar(100), lastname varchar(100), email varchar(150), roleid int)
returns varchar(30) as $$
begin 
	insert into users(username, password, firstname, lastname, email, roleid) 
		values(username, password, firstname, lastname, email, roleid);
	return 'Successfully added user.';
end;
$$ language 'plpgsql';
--select insert_user('jessshi8', 'P4$$w0rd', 'Jessica', 'Shi', 'jessicaaa.shi@gmail.com', 1);

-- deletes user by userid 
create or replace function delete_user(id int)
returns varchar(30) as $$
begin 
	delete from users where userid=id;
	return 'Successfully deleted user.';
end;
$$ language 'plpgsql';
--select delete_user(1);

-- return user role
create or replace function get_user_role(id int)
returns varchar(10) as $$
begin 
	return (select role from user_roles where roleid=id);
end;
$$ language 'plpgsql';

-- inserts into requests, request_status, and request_type
create or replace function insert_request(amount double precision, submitted timestamp, resolved timestamp, 
	description varchar(250), receipt bytea, authorid int, resolverid int, statusid int, typeid int)
returns varchar(30) as $$
begin
	insert into requests(amount, submitted, resolved, description, receipt, authorid, resolverid, statusid, typeid)
		values(amount, submitted, resolved, description, receipt, authorid, resolverid, statusid, typeid);
	return 'Successfully added request.';
end;
$$ language 'plpgsql';
--select insert_request(cast(150.85 as double precision), cast(current_timestamp as timestamp), null, 
--	'no description', null, 82, 1, 1, 1);

-- deletes request by requestid
create or replace function delete_request(id int)
returns varchar(30) as $$
begin 
	delete from requests where requestid=id;
	return 'Successfully deleted request.';
end;
$$ language 'plpgsql';
--select delete_request(1);

-- return request status
create or replace function get_request_status(id int)
returns varchar(10) as $$
begin 
	return (select status from request_status where statusid=id);
end;
$$ language 'plpgsql';

-- return request type
create or replace function get_request_type(id int)
returns varchar(25) as $$
begin 
	return (select type from request_type where typeid=id);
end;
$$ language 'plpgsql';

-- many to many relationship tracker
create or replace function add_relationship(username varchar(50), id int)
returns varchar(35) as $$
begin 
	insert into user_request_xref values(username, id);
	return 'Successfully added relationship.';
end;
$$ language 'plpgsql';
-----------------------------------------------------------------------------------------------------
-- Populating lookup tables
-- USER TYPES
insert into user_roles values(0, 'MANAGER');
insert into user_roles values(1, 'EMPLOYEE');
-- REQUEST STATUSES
insert into request_status values(0, 'DENIED');
insert into request_status values(1, 'PENDING');
insert into request_status values(2, 'APPROVED');
-- REQUEST TYPES
insert into request_type values(1, 'TRAVEL');
insert into request_type values(2, 'MILEAGE & GAS');
insert into request_type values(3, 'LODGING');
insert into request_type values(4, 'MEALS & ENTERTAINMENT');
insert into request_type values(5, 'MEDICAL');
insert into request_type values(6, 'OTHER');
-----------------------------------------------------------------------------------------------------
-- Manager credentials
select insert_user('jessshi8', '51c235b349dd4af59b9f2ae219cae37263dcf084599cb8537beec0cf19d8b82b', 'Jessica', 'Shi', 'jessica.shi@revature.net', 0);

-- View tables 
select * from users order by userid asc;
select * from requests order by requestid asc;
select * from user_roles;
select * from request_status;
select * from request_type;
select * from user_request_xref order by requestid asc;

-- Join tables
-- users and user_roles
select userid, username, password, firstname, lastname, email, r.roleid, r.role 
	from users u inner join user_roles r on u.roleid = r.roleid order by userid asc;
-- requests, request_type, and request_status
select requestid, amount, submitted, resolved, description, receipt, authorid, resolverid, requestid, s.statusid, s.status, t.typeid, t.type 
	from ((requests r inner join request_status s on r.statusid = s.statusid) 
		inner join request_type t on r.typeid = t.typeid) order by requestid asc;
-- requests, users, request_type, and request_status
select requestid, amount, submitted, resolved, description, receipt, authorid, resolverid, requestid, s.statusid, s.status, t.typeid, t.type 
	from ((((requests r inner join users a on r.authorid = a.userid)
		inner join users b on r.resolverid = b.userid)
			inner join request_status s on r.statusid = s.statusid)
				inner join request_type t on r.typeid = t.typeid) order by requestid asc;