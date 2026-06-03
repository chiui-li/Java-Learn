use website;

-- 用户表
create table
    if not exists users (
        id int primary key auto_increment,
        username varchar(255) not null,
        password varchar(255) not null,
        email varchar(255) not null,
        created_at timestamp default current_timestamp,
        updated_at timestamp default current_timestamp on update current_timestamp,
        password_updated_at timestamp default current_timestamp
    );

create trigger update_password_updated_at before
update on users for each row begin if old.password != new.password then
set
    new.password_updated_at = current_timestamp;

end if;

end;

END;

-- posts表
create table
    if not exists posts (
        id int primary key auto_increment,
        user_id int not null,
        title varchar(255) not null unique,
        content text not null,
        created_at timestamp default current_timestamp,
        updated_at timestamp default current_timestamp on update current_timestamp,
        view_count int default 0,
        post_type enum ('article', 'images') default 'article',
        draft_content text,
        draft_title varchar(255),
        draft_updated_at timestamp default current_timestamp,
        status enum ('draft', 'published') default 'draft',
        foreign key (user_id) references users (id) on delete cascade
    );

-- 当草稿内容或标题发生变化时，更新草稿更新时间
-- 当草稿内容或标题发生变化时，更新草稿更新时间
create trigger update_draft_updated_at before
update on posts for each row begin if old.draft_content != new.draft_content
or old.draft_title != new.draft_title then
set
    new.draft_updated_at = current_timestamp;

end if;

end;

-- 当状态从草稿变为发布时，更新发布时间和内容
create trigger update_status before
update on posts for each row begin if new.status = 'published'
and old.status != 'published' then
set
    new.updated_at = current_timestamp;

set
    new.title = new.draft_title;

set
    new.content = new.draft_content;

end if;

end;

create table
    if not exists tags (
        id int primary key auto_increment,
        name varchar(255) not null unique,
        created_at timestamp default current_timestamp,
        updated_at timestamp default current_timestamp on update current_timestamp,
        foreign key (user_id) references users (id) on delete cascade
    );

create table
    if not exists post_tags (
        post_id int not null,
        tag_id int not null,
        primary key (post_id, tag_id),
        foreign key (post_id) references posts (id) on delete cascade,
        foreign key (tag_id) references tags (id) on delete cascade
    );

create table
    if not exists comments (
        id int primary key auto_increment,
        post_id int not null,
        comment_id int,
        user_id int not null,
        content text not null,
        created_at timestamp default current_timestamp,
        updated_at timestamp default current_timestamp on update current_timestamp,
        foreign key (post_id) references posts (id) on delete cascade,
        foreign key (user_id) references users (id) on delete cascade,
        foreign key (comment_id) references comments (id) on delete cascade
    );

create table
    if not exists likes (
        id int primary key auto_increment,
        post_id int not null,
        user_id int not null,
        created_at timestamp default current_timestamp,
        foreign key (post_id) references posts (id) on delete cascade,
        foreign key (user_id) references users (id) on delete cascade
    );

create table
    if not exists friend_links (
        id int primary key auto_increment,
        user_id int not null,
        link varchar(255) not null,
        created_at timestamp default current_timestamp,
        updated_at timestamp default current_timestamp on update current_timestamp,
        foreign key (user_id) references users (id) on delete cascade
    );

create table
    if not exists user_profiles (
        id int primary key auto_increment,
        user_id int not null,
        bio text,
        avatar_url varchar(255),
        created_at timestamp default current_timestamp,
        updated_at timestamp default current_timestamp on update current_timestamp,
        realEmail varchar(255),
        phone varchar(20),
        github varchar(255),
        base varchar(255),
        foreign key (user_id) references users (id) on delete cascade
    );

create table
    if not exists images (
        id int primary key auto_increment,
        post_id int not null,
        url varchar(255) not null,
        created_at timestamp default current_timestamp,
        updated_at timestamp default current_timestamp on update current_timestamp,
        foreign key (post_id) references posts (id) on delete cascade
    );