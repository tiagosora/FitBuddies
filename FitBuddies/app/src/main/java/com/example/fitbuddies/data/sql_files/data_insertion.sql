-- Clear all tables before inserting data
TRUNCATE TABLE challenge_media RESTART IDENTITY CASCADE;
TRUNCATE TABLE dares RESTART IDENTITY CASCADE;
TRUNCATE TABLE challenges RESTART IDENTITY CASCADE;
TRUNCATE TABLE friendships RESTART IDENTITY CASCADE;
TRUNCATE TABLE users RESTART IDENTITY CASCADE;

-- Insert 20 Users
INSERT INTO users (userId, email, password, firstName, lastName, challengesCompleted, distanceTraveled, caloriesBurned, profilePictureUrl)
VALUES
-- Main users
(gen_random_uuid(), 'user1', 'pass', 'Tiago', 'Sora', 10, 50.5, 1500, 'https://example.com/profiles/user1.jpg'),
(gen_random_uuid(), 'user2', 'pass', 'Lili', 'Ana', 12, 70.0, 1800, 'https://example.com/profiles/user2.jpg'),
(gen_random_uuid(), 'user3', 'pass', 'Lia', 'Vibes', 8, 30.0, 1200, 'https://example.com/profiles/user3.jpg'),
-- Other users
(gen_random_uuid(), 'user4@example.com', 'password4', 'User', 'Four', 0, 0.0, 0, 'https://example.com/profiles/null_user.jpg'),
(gen_random_uuid(), 'user5@example.com', 'password5', 'User', 'Five', 0, 0.0, 0, 'https://example.com/profiles/null_user.jpg'),
(gen_random_uuid(), 'user6@example.com', 'password6', 'User', 'Six', 0, 0.0, 0, 'https://example.com/profiles/null_user.jpg'),
(gen_random_uuid(), 'user7@example.com', 'password7', 'User', 'Seven', 0, 0.0, 0, 'https://example.com/profiles/null_user.jpg'),
(gen_random_uuid(), 'user8@example.com', 'password8', 'User', 'Eight', 0, 0.0, 0, 'https://example.com/profiles/null_user.jpg'),
(gen_random_uuid(), 'user9@example.com', 'password9', 'User', 'Nine', 0, 0.0, 0, 'https://example.com/profiles/null_user.jpg'),
(gen_random_uuid(), 'user10@example.com', 'password10', 'User', 'Ten', 0, 0.0, 0, 'https://example.com/profiles/null_user.jpg'),
(gen_random_uuid(), 'user11@example.com', 'password11', 'User', 'Eleven', 0, 0.0, 0, 'https://example.com/profiles/null_user.jpg'),
(gen_random_uuid(), 'user12@example.com', 'password12', 'User', 'Twelve', 0, 0.0, 0, 'https://example.com/profiles/null_user.jpg'),
(gen_random_uuid(), 'user13@example.com', 'password13', 'User', 'Thirteen', 0, 0.0, 0, 'https://example.com/profiles/null_user.jpg'),
(gen_random_uuid(), 'user14@example.com', 'password14', 'User', 'Fourteen', 0, 0.0, 0, 'https://example.com/profiles/null_user.jpg'),
(gen_random_uuid(), 'user15@example.com', 'password15', 'User', 'Fifteen', 0, 0.0, 0, 'https://example.com/profiles/null_user.jpg'),
(gen_random_uuid(), 'user16@example.com', 'password16', 'User', 'Sixteen', 0, 0.0, 0, 'https://example.com/profiles/null_user.jpg'),
(gen_random_uuid(), 'user17@example.com', 'password17', 'User', 'Seventeen', 0, 0.0, 0, 'https://example.com/profiles/null_user.jpg'),
(gen_random_uuid(), 'user18@example.com', 'password18', 'User', 'Eighteen', 0, 0.0, 0, 'https://example.com/profiles/null_user.jpg'),
(gen_random_uuid(), 'user19@example.com', 'password19', 'User', 'Nineteen', 0, 0.0, 0, 'https://example.com/profiles/null_user.jpg'),
(gen_random_uuid(), 'user20@example.com', 'password20', 'User', 'Twenty', 0, 0.0, 0, 'https://example.com/profiles/null_user.jpg');

-- Insert Friendships
-- Main users are friends with each other
INSERT INTO friendships (userId, friendId, isAccepted, creationDate)
VALUES
((SELECT userId FROM users WHERE email = 'user1'), (SELECT userId FROM users WHERE email = 'user2'), TRUE, now()),
((SELECT userId FROM users WHERE email = 'user2'), (SELECT userId FROM users WHERE email = 'user3'), TRUE, now()),
((SELECT userId FROM users WHERE email = 'user3'), (SELECT userId FROM users WHERE email = 'user1'), TRUE, now());

-- Friendships between main users and other users
DO $$
DECLARE
    mainUsers UUID[];
    otherUsers UUID[];
    mainUser UUID;
    otherUser UUID;
BEGIN
    SELECT array_agg(userId) INTO mainUsers FROM users WHERE email IN ('user1', 'user2', 'user3');
    SELECT array_agg(userId) INTO otherUsers FROM users WHERE email NOT IN ('user1', 'user2', 'user3');

    FOREACH mainUser IN ARRAY mainUsers LOOP
        FOREACH otherUser IN ARRAY otherUsers LOOP
            INSERT INTO friendships (userId, friendId, isAccepted, creationDate)
            VALUES (mainUser, otherUser, (random() < 0.5), now());
        END LOOP;
    END LOOP;
END $$;

-- Insert Challenges
DO $$
DECLARE
    mainUsers UUID[];
    types TEXT[] := ARRAY['Cycling', 'Exercise', 'Running', 'Swimming', 'Walking'];
BEGIN
    SELECT array_agg(userId) INTO mainUsers FROM users WHERE email IN ('user1', 'user2', 'user3');

    FOR i IN 1..50 LOOP
        INSERT INTO challenges (challengeId, title, description, type, daredById, creationDate, deadlineDate)
        VALUES (
            gen_random_uuid(),
            'Challenge ' || i,
            'This is challenge ' || i,
            types[(floor(random() * array_length(types, 1))::int) + 1],
            mainUsers[(floor(random() * array_length(mainUsers, 1))::int) + 1],
            now(),
            now() + interval '7 days'
        );
    END LOOP;
END $$;

-- Insert Dares
DO $$
DECLARE
    challenges UUID[];
    users UUID[];
    challengeIndex INT;
    userIndex INT;
BEGIN
    SELECT array_agg(challengeId) INTO challenges FROM challenges;
    SELECT array_agg(userId) INTO users FROM users;

    FOR i IN 1..50 LOOP
        challengeIndex := floor(random() * array_length(challenges, 1) + 1)::int;
        userIndex := floor(random() * array_length(users, 1) + 1)::int;

        INSERT INTO dares (dareId, challengeId, daredToId, isAccepted, isCompleted, completionDate, completionRate)
        VALUES (
            gen_random_uuid(),
            challenges[challengeIndex],
            users[userIndex],
            (random() < 0.5), -- Boolean value
            (random() < 0.5), -- Boolean value
            now(),
            floor(random() * 100)::int -- Integer completion rate
        );
    END LOOP;
END $$;

-- Update all dares with isAccepted = false to ensure isCompleted = false
UPDATE dares SET isCompleted = FALSE WHERE isAccepted = FALSE;
UPDATE dares SET isAccepted = TRUE WHERE isCompleted = TRUE;
UPDATE dares SET completionRate = 100 WHERE isCompleted = TRUE;
UPDATE dares SET completionRate = 0 WHERE isAccepted = FALSE;

-- Insert Challenge Media
DO $$
DECLARE
    challenges UUID[];
    challengeIndex INT;
BEGIN
    SELECT array_agg(challengeId) INTO challenges FROM challenges;

    FOR i IN 1..150 LOOP
        challengeIndex := floor(random() * array_length(challenges, 1) + 1)::int;

        INSERT INTO challenge_media (mediaId, challengeId, mediaUrl, mediaType, timestamp)
        VALUES (
            gen_random_uuid(),
            challenges[challengeIndex],
            'https://example.com/media/challenge' || i || '.jpg',
            'PHOTO',
            now()
        );
    END LOOP;
END $$;