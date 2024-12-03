-- Clear all tables before inserting data
TRUNCATE TABLE challenge_media RESTART IDENTITY CASCADE;
TRUNCATE TABLE dares RESTART IDENTITY CASCADE;
TRUNCATE TABLE challenges RESTART IDENTITY CASCADE;
TRUNCATE TABLE friendships RESTART IDENTITY CASCADE;
TRUNCATE TABLE users RESTART IDENTITY CASCADE;

-- Insert 20 Users
INSERT INTO users (user_id, email, password, first_name, last_name, challenges_completed, distance_traveled, calories_burned, profile_picture_url)
VALUES
-- Main users
(gen_random_uuid(), 'user1@example.com', 'password1', 'John', 'Doe', 10, 50.5, 1500, 'https://example.com/profiles/user1.jpg'),
(gen_random_uuid(), 'user2@example.com', 'password2', 'Jane', 'Smith', 12, 70.0, 1800, 'https://example.com/profiles/user2.jpg'),
(gen_random_uuid(), 'user3@example.com', 'password3', 'Alice', 'Johnson', 8, 30.0, 1200, 'https://example.com/profiles/user3.jpg'),
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
INSERT INTO friendships (user_id, friend_id, is_accepted, creation_date)
VALUES
((SELECT user_id FROM users WHERE email = 'user1@example.com'), (SELECT user_id FROM users WHERE email = 'user2@example.com'), TRUE, now()),
((SELECT user_id FROM users WHERE email = 'user2@example.com'), (SELECT user_id FROM users WHERE email = 'user3@example.com'), TRUE, now()),
((SELECT user_id FROM users WHERE email = 'user3@example.com'), (SELECT user_id FROM users WHERE email = 'user1@example.com'), TRUE, now());

-- Friendships between main users and other users
DO $$
DECLARE
    main_users UUID[];
    other_users UUID[];
    main_user UUID;
    other_user UUID;
BEGIN
    SELECT array_agg(user_id) INTO main_users FROM users WHERE email IN ('user1@example.com', 'user2@example.com', 'user3@example.com');
    SELECT array_agg(user_id) INTO other_users FROM users WHERE email NOT IN ('user1@example.com', 'user2@example.com', 'user3@example.com');

    FOREACH main_user IN ARRAY main_users LOOP
        FOREACH other_user IN ARRAY other_users LOOP
            INSERT INTO friendships (user_id, friend_id, is_accepted, creation_date)
            VALUES (main_user, other_user, (random() < 0.5), now());
        END LOOP;
    END LOOP;
END $$;

-- Insert Challenges
DO $$
DECLARE
    main_users UUID[];
    types TEXT[] := ARRAY['Cycling', 'Exercise', 'Running', 'Swimming', 'Walking'];
BEGIN
    SELECT array_agg(user_id) INTO main_users FROM users WHERE email IN ('user1@example.com', 'user2@example.com', 'user3@example.com');

    FOR i IN 1..50 LOOP
        INSERT INTO challenges (challenge_id, title, description, type, dared_by_id, creation_date, deadline_date)
        VALUES (
            gen_random_uuid(),
            'Challenge ' || i,
            'This is challenge ' || i,
            types[(floor(random() * array_length(types, 1))::int) + 1],
            main_users[(floor(random() * array_length(main_users, 1))::int) + 1],
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
    challenge_index INT;
    user_index INT;
BEGIN
    SELECT array_agg(challenge_id) INTO challenges FROM challenges;
    SELECT array_agg(user_id) INTO users FROM users;

    FOR i IN 1..50 LOOP
        challenge_index := floor(random() * array_length(challenges, 1) + 1)::int;
        user_index := floor(random() * array_length(users, 1) + 1)::int;

        INSERT INTO dares (dare_id, challenge_id, dared_to_id, is_accepted, is_completed, completion_date, completion_rate)
        VALUES (
            gen_random_uuid(),
            challenges[challenge_index],
            users[user_index],
            (random() < 0.5), -- Boolean value
            (random() < 0.5), -- Boolean value
            now(),
            floor(random() * 100)::int -- Integer completion rate
        );
    END LOOP;
END $$;

-- All dares with isAccepted = false, must have isCompleted = false
UPDATE dares SET is_completed = FALSE WHERE is_accepted = FALSE;


-- Insert Challenge Media
DO $$
DECLARE
    challenges UUID[];
    challenge_index INT;
BEGIN
    SELECT array_agg(challenge_id) INTO challenges FROM challenges;

    FOR i IN 1..150 LOOP
        challenge_index := floor(random() * array_length(challenges, 1) + 1)::int;

        INSERT INTO challenge_media (media_id, challenge_id, media_url, media_type, timestamp)
        VALUES (
            gen_random_uuid(),
            challenges[challenge_index],
            'https://example.com/media/challenge' || i || '.jpg',
            'PHOTO',
            now()
        );
    END LOOP;
END $$;
