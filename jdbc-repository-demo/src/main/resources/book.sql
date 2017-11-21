DROP TABLE IF EXISTS "public"."book";
CREATE TABLE "public"."book" (
	"id" serial,
	"book_id" numeric(19,10),
	"title" varchar(255) COLLATE "default"
)
WITH (OIDS=FALSE);
ALTER TABLE "public"."book" OWNER TO "postgres";