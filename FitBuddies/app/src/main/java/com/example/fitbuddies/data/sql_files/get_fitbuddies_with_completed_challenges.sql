DROP FUNCTION IF EXISTS get_fitbuddies_with_completed_challenges(uuid);

CREATE OR REPLACE FUNCTION get_fitbuddies_with_completed_challenges(user_id UUID)
RETURNS TABLE (
    fitbuddyid UUID,
    firstname TEXT,
    lastname TEXT,
    profilepictureurl TEXT,
    challengescompletedcount INTEGER
) AS $$
BEGIN
    RETURN QUERY
    WITH fitbuddies AS (
        SELECT
            CASE
                WHEN f.userId = user_id THEN f.friendId
                WHEN f.friendId = user_id THEN f.userId
            END AS fitbuddyid
        FROM friendships f
        WHERE (f.userId = user_id OR f.friendId = user_id)
          AND f.isAccepted = TRUE
    ),
    challenges_count AS (
        SELECT
            d.daredToId AS fitbuddyid,
            COUNT(*)::INTEGER AS challengescompletedcount
        FROM dares d
        WHERE d.isCompleted = TRUE
        GROUP BY d.daredToId
    )
    SELECT
        fb.fitbuddyid,
        u.firstName AS firstname,
        u.lastName AS lastname,
        u.profilepictureurl AS profilepictureurl,
        COALESCE(cc.challengescompletedcount, 0) AS challengescompletedcount
    FROM fitbuddies fb
    LEFT JOIN users u ON fb.fitbuddyid = u.userId
    LEFT JOIN challenges_count cc ON fb.fitbuddyid = cc.fitbuddyid;
END;
$$ LANGUAGE plpgsql;

