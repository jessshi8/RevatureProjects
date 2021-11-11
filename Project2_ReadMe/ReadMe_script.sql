-- Users Table
-- drop table users cascade;
create table users(
	username varchar(50) primary key,
	password varchar(64) not null,
	firstname varchar(50) not null,
	lastname varchar(50) not null,
	email varchar(150) not null,
	roleid varchar(20) not null
);

-- Books Table
-- drop table books cascade;
create table books(
	isbn varchar(15) primary key,
	book_cover bytea,
	title varchar(50) not null,
	author varchar(50) not null,
	publisher varchar(50) not null,
	published varchar(50) not null,
	genre varchar(20) not null,
	price double precision not null,
	summary varchar(500) not null
);

alter table books alter column book_cover type varchar;

-- Populate books table
--create or replace function insert_book(isbn varchar(15), book_cover bytea, title varchar(50), author varchar(50), 
--	publisher varchar(50), published varchar(50), genre varchar(20), price double precision, summary varchar(500))
--returns varchar(25) as $$
--begin
--	insert into books(isbn, book_cover, title, author, publisher, published, genre, price, summary) 
--		values(isbn, book_cover, title, author, publisher, published, genre, price, summary);
--	return 'Successfully added book.';
--end;
--$$ language 'plpgsql';

--select insert_book('9781503222687', null, 'Alice''s Adventures in Wonderland', 'Lewis Carroll', 'Macmillan', '26 November 1965', 'Fantasy', 5.97, 'Alice''s Adventures in Wonderland by Lewis Carroll is a story about Alice who falls down a rabbit hole and lands into a fantasy world that is full of weird, wonderful people and animals.');
--select insert_book('9780340606513', null, 'Schindler''s List', 'Thomas Keneally', 'Simon & Schuster', '18 October 1982', 'History', 16.99, 'In the shadow of Auschwitz, a flamboyant German industrialist grew into a living legend to the Jews of Cracow. He was a womaniser, a heavy drinker and a bon viveur, but to them he became a saviour. This is the extraordinary story of Oskar Schindler, who risked his life to protect Jews in Nazi-occupied Poland and who was transformed by the war into a man with a mission, a compassionate angel of mercy.');
--select insert_book('9780743273565', null, 'The Great Gatsby', 'F. Scott Fitzgerald', 'Charles Scriber''s Sons', '10 April 1925', 'Historical Fiction', 5.97, 'Set in the Jazz Age on Long Island, the novel depicts narrator Nick Carraway''s interactions with mysterious millionaire Jay Gatsby and Gatsby''s obsession to reunite with his former lover, Daisy Buchanan.');
--select insert_book('9781501180996', null, 'The Outsider', 'Stephen King', 'Simon & Schuster', '22 May 2018', 'Mystery', 11.20, 'An unspeakable crime takes place involving the murder and violation of a small boy. Ralph Anderson is the detective on the case, and he arrests a local man, Terry Maitland. It''s an easy arrest and their evidence is airtight. The problem is, cracks in the case begin to appear when it seems that perhaps Maitland has an equally airtight alibi.');
--select insert_book('9781402726002', null, 'Adventures of Huckleberry Finn', 'Mark Twain', 'Chatto & Windus', '10 December 1884', 'Humor', 8.99, 'The novel tells the story of Huckleberry Finn''s escape from his alcoholic and abusive father and Huck''s adventurous journey down the Mississippi River together with the runaway slave Jim.');
--select insert_book('9780142424179', null, 'The Fault in Our Stars', 'John Green', 'Penguin Books', '10 January 2012', 'Romance', 6.10, 'Despite the tumor-shrinking medical miracle that has bought her a few years, Hazel has never been anything but terminal, her final chapter inscribed upon diagnosis. But when a gorgeous plot twist named Augustus Waters suddenly appears at Cancer Kid Support Group, Hazel’s story is about to be completely rewritten.');
--select insert_book('9781841953922', null, 'Life of Pi', 'Yann Martel', 'Knopf Canada', '11 September 2001', 'Adventure', 8.99, 'After the tragic sinking of a cargo ship, a solitary lifeboat remains bobbing on the wild, blue Pacific. The only survivors from the wreck are a sixteen year-old boy named Pi, a hyena, a zebra (with a broken leg), a female orangutan - and a 450-pound Royal Bengal tiger.');
--select insert_book('9780316017930', null, 'Outliers: The Story of Success', 'Malcolm Gladwell', 'Little, Brown and Company', '18 November 2008', 'Self-Help', 14.03, 'Malcolm Gladwell takes us on an intellectual journey through the world of "outliers" - the best and the brightest, the most famous, and the most successful. He asks the question: What makes high-achievers different? His answer is that we pay too much attention to what successful people are like, and too little attention to where they are from: That is, their culture, their family, their generation, and the idiosyncratic experiences of their upbringing.');
--select insert_book('9781538728628', null, 'The Wish', 'Nicholas Sparks', 'Grand Central Publishing', '28 September 2021', 'Romance', 13.24, '1996 was the year that changed everything for Maggie Dawes. Sent away at sixteen to live with an aunt she barely knew in Ocracoke, she could think only of the friends and family she left behind until she met Bryce Trickett, one of the few teenagers on the island. Handsome, genuine, and newly admitted to West Point, Bryce showed her how much there was to love about the wind-swept beach town—and introduced her to photography, a passion that would define the rest of her life.');
--select insert_book('9780747546245', null, 'Harry Potter and the Goblet of Fire', 'J.K. Rowling', 'Pottermore Publishing', '8 July 2000', 'Fantasy', 12.99, 'It follows Harry Potter, a wizard in his fourth year at Hogwarts School of Witchcraft and Wizardry, and the mystery surrounding the entry of Harry''s name into the Triwizard Tournament, in which he is forced to compete.');
--select insert_book('9781467761796', null, 'Out Of Darkness', 'Ashley Hope Pérez', 'Lerner Publishing Group', '01 September 2015', 'Fiction', 18.99, 'New London, Texas. 1937. Naomi Vargas and Wash Fuller know about the lines in East Texas as well as anyone. They know the signs that mark them. They know the people who enforce them. But sometimes the attraction between two people is so powerful it breaks through even the most entrenched color lines. And the consequences can be explosive.');
--select insert_book('9780062447609', null, 'Cinnamon Girl', 'Juan Felipe Herrera', 'HarperCollins', '09 August 2005', 'Poetry', 8.99, 'When the towers fall, New York City is blanketed by dust. On the Lower East Side, Yolanda, the Cinnamon Girl, makes her manda, her promise, to gather as much of it as she can. Maybe returning the dust to Ground Zero can comfort all the voices. Maybe it can help Uncle DJ open his eyes again.');
--select insert_book('9780547807478', null, 'The Lightning Dreamer', 'Margarita Engle', 'Houghton Mifflin Harcourt', '09 September 2015', 'Poetry', 7.99, 'Opposing slavery in Cuba in the nineteenth century was dangerous. The most daring abolitionists were poets who veiled their work in metaphor. Of these, the boldest was Gertrudis Gómez de Avellaneda, nicknamed Tula. In passionate, accessible verses of her own, Engle evokes the voice of this book-loving feminist and abolitionist who bravely resisted an arranged marriage at the age of fourteen, and was ultimately courageous enough to fight against injustice.');
--select insert_book('9781416985877', null, 'Dream of Significant Girls', 'Cristina García', 'Simon & Schuster Children Publishing', '22 May 2012', 'Literature Fiction', 8.99, 'Shirin is an Iranian princess; Ingrid, a German-Canadian eccentric; and Vivien, a Cuban-Jewish New Yorker culinary phenom. The three are roommates at a Swiss boarding school, where they spend their summers learning more than French and European culture. As the girls’ paths cross and merge—summers together, school years separate—they navigate social and cultural differences and learn the confusing and conflicting legacies of their families’ pasts.');
--select insert_book('9780545591621', null, 'Shadowshaper', 'Daniel José Older', 'Scholastic Inc', '30 June 2015', 'Fantasy', 7.49, 'Paint a mural. Start a battle. Change the world. Sierra Santiago planned an easy summer of making art and hanging out with her friends. But then a corpse crashes the first party of the season. Her stroke-ridden grandfather starts apologizing over and over. And when the murals in her neighborhood begin to weep real tears... Well, something more sinister than the usual Brooklyn ruckus is going on.');
--select insert_book('9780062882783', null, 'Clap When You Land', 'Elizabeth Acevedo', 'HarperCollins', '05 May 2020', 'Literature Fiction', 15.99, 'Camino Rios lives for the summers when her father visits her in the Dominican Republic. But this time, on the day when his plane is supposed to land, Camino arrives at the airport to see crowds of crying people.');
--select insert_book('9780062662859', null, 'With The Fire On High', 'Elizabeth Acevedo', 'HarperCollins', '07 May 2019', 'Literature Fiction', 10.99, 'Ever since she got pregnant freshman year, Emoni Santiago’s life has been about making the tough decisions—doing what has to be done for her daughter and her abuela.');
--select insert_book('9781643751207', null, 'Furia', 'Yamile Saied Méndez', 'Algonquin Books', '15 September 2020', 'Literature Fiction', 11.49, 'At home, she is a careful daughter, living within her mother’s narrow expectations, in her rising-soccer-star brother’s shadow, and under the abusive rule of her short-tempered father.');
--select insert_book('9780375897429', null, 'I Will Save You', 'Matt de la Peña', 'Random House Children Books', '08 November 2011', 'Literature Fiction', 9.99, 'Kidd is running from his past and his future. No mom, no dad, and there is nothing for him at the group home but therapy. He do not belong at the beach where he works either, unless he finds a reason to stay. Olivia is blond hair, blue eyes, rich dad. The prettiest girl in Cardiff.');
--select insert_book('9781947627062', null, 'They Call Me Guero', 'David Bowles', 'Cinco Puntos Press', '27 November 2018', 'Poetry', 18.82, 'Twelve-year-old Guero, a red-headed, freckled Mexican American border kid, discovers the joy of writing poetry, thanks to his seventh grade English teacher.');

select * from users;
select * from books;
select * from users_orders;
select * from users_cart;
select * from users_bookmarks;

--update books set book_cover = 'https://i1.wp.com/americanwritersmuseum.org/wp-content/uploads/2018/02/CK-3.jpg?resize=267%2C400&ssl=1' where isbn='9780743273565';

