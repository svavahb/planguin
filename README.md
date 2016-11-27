# hbv1

=================
Að keyra forritið
=================
-Opna í IntelliJ
-Installa Maven dependecies (ætti að koma upp gluggi)
-Nota jdk8 sem project sdk
-Þarf kannski: Fara í settings, java compiler og breyta project target bytecode version í 1.8
-Kannski líka: Fara í project structure, modules, breyta language level í 8
-Breyta gagnagrunnstenginu í Repository.class, í smiðnum
-Setja Application.class sem það sem keyrir
-Keyra úr IntelliJ!


Líka hægt að fara á http://planguin.herokuapp.com og sjá þar nýjustu útgáfuna af vefnum

===================
SQL DATABASE SCRIPT
===================

|------|
| USER |
|------|
-- Table: public."user"

-- DROP TABLE public."user";

CREATE TABLE public."user"
(
  id integer NOT NULL DEFAULT nextval('"User_id_seq"'::regclass),
  password text,
  photo text,
  username text,
  school text,
  CONSTRAINT "User_pkey" PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public."user"
  OWNER TO postgres;


|--------------|
| SCHEDULEITEM |
|--------------|
-- Table: public."scheduleItem"

-- DROP TABLE public."scheduleItem";

CREATE TABLE public."scheduleItem"
(
  color text,
  description text,
  "endTime" timestamp without time zone,
  id integer NOT NULL DEFAULT nextval('"ScheduleItem_id_seq"'::regclass),
  location text,
  "startTime" timestamp without time zone,
  title text,
  userid integer,
  "weekNo" integer,
  year integer,
  CONSTRAINT "ScheduleItem_pkey" PRIMARY KEY (id),
  CONSTRAINT userid FOREIGN KEY (userid)
      REFERENCES public."user" (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public."scheduleItem"
  OWNER TO postgres;

-- Index: public.fki_userid

-- DROP INDEX public.fki_userid;

CREATE INDEX fki_userid
  ON public."scheduleItem"
  USING btree
  (userid);


|-------|
| GROUP |
|-------|

-- Table: public."group"

-- DROP TABLE public."group";

CREATE TABLE public."group"
(
  id integer NOT NULL DEFAULT nextval('"Group_id_seq"'::regclass),
  name text,
  CONSTRAINT "Group_pkey" PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public."group"
  OWNER TO postgres;




|---------|
| FILTERS |
|---------|

-- Table: public.filters

-- DROP TABLE public.filters;

CREATE TABLE public.filters
(
  id integer NOT NULL DEFAULT nextval('"Filters_id_seq"'::regclass),
  itemid integer,
  userid integer,
  name text,
  CONSTRAINT "Filters_pkey" PRIMARY KEY (id),
  CONSTRAINT itemfilter_fkey FOREIGN KEY (itemid)
      REFERENCES public."scheduleItem" (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT user_fkey FOREIGN KEY (userid)
      REFERENCES public."user" (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT userfilter_fkey FOREIGN KEY (userid)
      REFERENCES public."user" (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.filters
  OWNER TO postgres;

-- Index: public.fki_itemfilter_fkey

-- DROP INDEX public.fki_itemfilter_fkey;

CREATE INDEX fki_itemfilter_fkey
  ON public.filters
  USING btree
  (itemid);

-- Index: public.fki_userfilter_fkey

-- DROP INDEX public.fki_userfilter_fkey;

CREATE INDEX fki_userfilter_fkey
  ON public.filters
  USING btree
  (userid);



|------------|
| FRIENDSHIP |
|------------|

-- Table: public.friendship

-- DROP TABLE public.friendship;

CREATE TABLE public.friendship
(
  id integer NOT NULL DEFAULT nextval('"Friendship_id_seq"'::regclass),
  userid1 integer,
  userid2 integer,
  CONSTRAINT "Friendship_pkey" PRIMARY KEY (id),
  CONSTRAINT user1_fkey FOREIGN KEY (userid1)
      REFERENCES public."user" (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT user2_fkey FOREIGN KEY (userid2)
      REFERENCES public."user" (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.friendship
  OWNER TO postgres;

-- Index: public.fki_user1_fkey

-- DROP INDEX public.fki_user1_fkey;

CREATE INDEX fki_user1_fkey
  ON public.friendship
  USING btree
  (userid1);

-- Index: public.fki_user2_fkey

-- DROP INDEX public.fki_user2_fkey;

CREATE INDEX fki_user2_fkey
  ON public.friendship
  USING btree
  (userid2);



|---------|
| MEMBERS |
|---------|

-- Table: public.members

-- DROP TABLE public.members;

CREATE TABLE public.members
(
  id integer NOT NULL DEFAULT nextval('"Members_id_seq"'::regclass),
  groupid integer,
  userid integer,
  CONSTRAINT "Members_pkey" PRIMARY KEY (id),
  CONSTRAINT group_fkey FOREIGN KEY (groupid)
      REFERENCES public."group" (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT member_fkey FOREIGN KEY (userid)
      REFERENCES public."user" (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.members
  OWNER TO postgres;

-- Index: public.fki_group_fkey

-- DROP INDEX public.fki_group_fkey;

CREATE INDEX fki_group_fkey
  ON public.members
  USING btree
  (groupid);

-- Index: public.fki_member_fkey

-- DROP INDEX public.fki_member_fkey;

CREATE INDEX fki_member_fkey
  ON public.members
  USING btree
  (userid);


|------|
| TAGS |
|------|

-- Table: public.tags

-- DROP TABLE public.tags;

CREATE TABLE public.tags
(
  userid integer,
  itemid integer,
  id integer NOT NULL DEFAULT nextval('"Tags_id_seq"'::regclass),
  CONSTRAINT tags_pkey PRIMARY KEY (id),
  CONSTRAINT item_fkey FOREIGN KEY (itemid)
      REFERENCES public."scheduleItem" (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT user_fkey FOREIGN KEY (userid)
      REFERENCES public."user" (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.tags
  OWNER TO postgres;

-- Index: public.fki_item_fkey

-- DROP INDEX public.fki_item_fkey;

CREATE INDEX fki_item_fkey
  ON public.tags
  USING btree
  (itemid);

-- Index: public.fki_user_fkey

-- DROP INDEX public.fki_user_fkey;

CREATE INDEX fki_user_fkey
  ON public.tags
  USING btree
  (userid);

