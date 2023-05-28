insert into USERS(username, password, email, role, status) values ('ssar', '1234', 'ssar@nate.com', 'USER', 'ACTIVE');
insert into USERS(username, password, email, role, status) values ('cos', '1234', 'cos@nate.com', 'MANAGER', 'ACTIVE');
insert into USERS(username, password, email, role, status) values ('love', '1234', 'love@nate.com', 'ADMIN', 'ACTIVE');


insert into BOARD(title, content, user_id) values ('제목입니다 1', '내용입니다 1', 1);
insert into BOARD(title, content, user_id) values ('제목입니다 2', '내용입니다 2', 2);
insert into BOARD(title, content, user_id) values ('제목입니다 3', '내용입니다 3', 3);

insert into REPLY_LIST(comment, board_id) values ('댓글1', 1);
insert into REPLY_LIST(comment, board_id) values ('댓글2', 1);
insert into REPLY_LIST(comment, board_id) values ('댓글3', 1);

commit ;
