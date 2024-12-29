DROP FUNCTION IF EXISTS get_last_completed_challenges(uuid[]);

CREATE OR REPLACE FUNCTION get_last_completed_challenges(fitbuddies_ids UUID[])
RETURNS TABLE (
    dareid UUID,
    daredtoid UUID,
    isaccepted BOOLEAN,
    iscompleted BOOLEAN,
    completiondate TIMESTAMP,
    completionrate FLOAT,
    challengeid UUID,
    title TEXT,
    description TEXT,
    type TEXT,
    daredbyid UUID,
    creationdate TIMESTAMP,
    deadlinedate TIMESTAMP
) AS $$
BEGIN
    RETURN QUERY
    WITH ranked_challenges AS (
        SELECT
            d.dareid AS dareid,
            d.daredtoid AS daredtoid,
            d.isaccepted AS isaccepted,
            d.iscompleted AS iscompleted,
            d.completiondate AS completiondate,
            d.completionrate::FLOAT AS completionrate, -- Cast completionrate to FLOAT
            c.challengeid AS challengeid,
            c.title AS title,
            c.description AS description,
            c.type AS type,
            c.daredbyid AS daredbyid,
            c.creationdate AS creationdate,
            c.deadlinedate AS deadlinedate,
            ROW_NUMBER() OVER (PARTITION BY d.daredtoid ORDER BY d.completiondate DESC) AS rank
        FROM dares d
        JOIN challenges c ON d.challengeid = c.challengeid
        WHERE d.daredtoid = ANY(fitbuddies_ids)
          AND d.iscompleted = TRUE
    )
    SELECT
        ranked_challenges.dareid,
        ranked_challenges.daredtoid,
        ranked_challenges.isaccepted,
        ranked_challenges.iscompleted,
        ranked_challenges.completiondate,
        ranked_challenges.completionrate,
        ranked_challenges.challengeid,
        ranked_challenges.title,
        ranked_challenges.description,
        ranked_challenges.type,
        ranked_challenges.daredbyid,
        ranked_challenges.creationdate,
        ranked_challenges.deadlinedate
    FROM ranked_challenges
    WHERE ranked_challenges.rank = 1;
END;
$$ LANGUAGE plpgsql;

-- SELECT * FROM get_last_completed_challenges(ARRAY['55c90301-2ff7-4f87-8d86-3875eeed95e3']::uuid[]);
