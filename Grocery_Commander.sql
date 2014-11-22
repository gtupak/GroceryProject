DROP TABLE IF EXISTS entries, usernames;

CREATE TABLE IF NOT EXISTS entries(
	id serial PRIMARY KEY,
	entry VARCHAR(50),
	creator VARCHAR(20),
	checker VARCHAR(20),
	checked BOOLEAN
);

CREATE TABLE IF NOT EXISTS usernames(
	id serial PRIMARY KEY,
	username VARCHAR(20)
);

INSERT INTO entries(id, entry, creator, checker, checked) VALUES (1, '5 eggs', 'Gabriel', NULL, false);
INSERT INTO entries(id, entry, creator, checker, checked) VALUES (2, '2 bags of flour', 'Gabriel', NULL, false);
INSERT INTO entries(id, entry, creator, checker, checked) VALUES (3, 'sugar', 'Christine', NULL, false);
INSERT INTO entries(id, entry, creator, checker, checked) VALUES (4, '5 apples (I <3 apples)', 'Gabriel', NULL, false);
INSERT INTO entries(id, entry, creator, checker, checked) VALUES (5, 'chocolate', 'Christine', NULL, false);

INSERT INTO usernames(id, username) VALUES (1, 'Gabriel');
INSERT INTO usernames(id, username) VALUES (2, 'Christine');

UPDATE entries SET checked=true WHERE id=1;