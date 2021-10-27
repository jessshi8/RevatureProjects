-- User Table
--drop table users;
create table users(
	username varchar(20) primary key,
	password varchar(20) not null,
	usertype varchar(10),
	firstname varchar(20),
	lastname varchar(20),
	dateofbirth varchar(10),
	phonenumber varchar(10)
);

-- Account Table
--drop table accounts;
create table accounts(
	accountid int primary key generated always as identity,
	accounttype varchar(10),
	status boolean,
	balance double precision
);

-- User_Account_Xref Table
-- drop table user_account_xref;
create table user_account_xref(
	username varchar(20) references users(username) on delete cascade,
	accountid int references accounts(accountid) on delete cascade
);
alter table user_account_xref add constraint compkey primary key(username, accountid);

-- insert user function
--drop function insert_user;
create or replace function insert_user(u_username varchar(20), u_password varchar(20), u_type varchar(10), 
	u_fname varchar(20), u_lname varchar(20), u_dob varchar(10), u_phonenum varchar(10))
returns varchar(30) as $$
begin 
	insert into users(username, password, usertype, firstname, lastname, dateofbirth, phonenumber)
		values(u_username, u_password, u_type, u_fname, u_lname, u_dob, u_phonenum);
	return 'Successfully added user.';
end;
$$ language 'plpgsql';

-- insert account function
--drop function insert_account;
create or replace function insert_account(a_type varchar(10), a_status boolean, a_balance double precision)
returns varchar(30) as $$
begin 
	insert into accounts(accounttype, status, balance) values(a_type, a_status, a_balance);
	return 'Successfully added account.';
end;
$$ language 'plpgsql';

-- get accountid of recently added account
create or replace function get_account_id()
returns int as $$
begin 
	return (select max(accountid) from accounts);
end;
$$ language 'plpgsql';

-- Joins
select * from users u left outer join user_account_xref x on u.username = x.username 
		left outer join accounts a on x.accountid = a.accountid;
		
-- view tables
select * from users;
select * from accounts;
select * from user_account_xref;
