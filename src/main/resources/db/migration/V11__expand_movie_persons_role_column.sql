-- Expand movie_persons.role column from ENUM/VARCHAR(20) to VARCHAR(30)
-- to support new roles: ACTRESS, COMPOSER, CINEMATOGRAPHER, EDITOR, VOICE_ACTOR, CAMEO
ALTER TABLE movie_persons MODIFY COLUMN role VARCHAR(30) NOT NULL;
