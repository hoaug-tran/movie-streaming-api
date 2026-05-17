ALTER TABLE movie_persons MODIFY COLUMN role VARCHAR(30) NOT NULL;

SET @constraint_name = (
  SELECT CONSTRAINT_NAME
  FROM information_schema.TABLE_CONSTRAINTS
  WHERE TABLE_SCHEMA = DATABASE()
    AND TABLE_NAME = 'movie_persons'
    AND CONSTRAINT_TYPE = 'UNIQUE'
  LIMIT 1
);

SET @sql = IF(
  @constraint_name IS NOT NULL,
  CONCAT('ALTER TABLE movie_persons DROP INDEX `', @constraint_name, '`'),
  'SELECT 1'
);

PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
