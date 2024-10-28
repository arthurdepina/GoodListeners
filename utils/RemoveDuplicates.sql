SELECT * 
FROM Albums
WHERE name IN (
    SELECT name
    FROM Albums
    GROUP BY name
    HAVING COUNT(name) > 1
);

SELECT COUNT(*) AS repeated_album_count
FROM (
    SELECT name
    FROM Albums
    GROUP BY name
    HAVING COUNT(name) > 1
) AS repeated_albums;

DELETE FROM Albums
WHERE ROWID NOT IN (
    SELECT MIN(ROWID)
    FROM Albums
    GROUP BY name
);
