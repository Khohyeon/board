insert into USERS(username, password, email, role, status) values ('ssar', '1234', 'ssar@nate.com', 'USER', 'ACTIVE');
insert into USERS(username, password, email, role, status) values ('cos', '1234', 'cos@nate.com', 'USER', 'ACTIVE');
insert into USERS(username, password, email, role, status) values ('love', '1234', 'love@nate.com', 'ADMIN', 'ACTIVE');


insert into BOARD(title, content) values ('제목1', '내용1');
insert into BOARD(title, content) values ('제목2', '내용2');
insert into BOARD(title, content) values ('제목3', '내용3');

insert into REPLY_LIST(comment, board_id) values ('댓글1', 1);
insert into REPLY_LIST(comment, board_id) values ('댓글2', 1);
insert into REPLY_LIST(comment, board_id) values ('댓글3', 1);