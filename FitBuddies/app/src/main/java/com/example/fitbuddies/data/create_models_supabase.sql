-- Drop dependent tables first to maintain referential integrity
DROP TABLE IF EXISTS challenge_media CASCADE;
DROP TABLE IF EXISTS dares CASCADE;
DROP TABLE IF EXISTS challenges CASCADE;
DROP TABLE IF EXISTS friendships CASCADE;
DROP TABLE IF EXISTS users CASCADE;

-- Drop the enum type if it exists
DO $$
BEGIN
    IF EXISTS (SELECT 1 FROM pg_type WHERE typname = 'media_type') THEN
        DROP TYPE media_type;
    END IF;
END $$;

-- Recreate the MediaType Enum
CREATE TYPE media_type AS ENUM ('PHOTO', 'VIDEO', 'MUSIC', 'LOCATION_TRACKING');

-- Recreate the Users Table
CREATE TABLE users (
    user_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    email TEXT NOT NULL UNIQUE,
    password TEXT NOT NULL,
    first_name TEXT NOT NULL,
    last_name TEXT NOT NULL,
    challenges_completed INTEGER DEFAULT 0,
    distance_traveled DOUBLE PRECISION DEFAULT 0.0,
    calories_burned INTEGER DEFAULT 0,
    profile_picture_url TEXT
);

-- Recreate the Friendships Table
CREATE TABLE friendships (
    user_id UUID REFERENCES users(user_id) ON DELETE CASCADE,
    friend_id UUID REFERENCES users(user_id) ON DELETE CASCADE,
    is_accepted BOOLEAN DEFAULT FALSE,
    creation_date TIMESTAMP DEFAULT now(),
    PRIMARY KEY (user_id, friend_id)
);

-- Recreate the Challenges Table
CREATE TABLE challenges (
    challenge_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    title TEXT NOT NULL,
    description TEXT NOT NULL,
    type TEXT NOT NULL,
    dared_by_id UUID REFERENCES users(user_id) ON DELETE CASCADE,
    creation_date TIMESTAMP DEFAULT now(),
    deadline_date TIMESTAMP
);

-- Recreate the ChallengeMedia Table
CREATE TABLE challenge_media (
    media_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    challenge_id UUID REFERENCES challenges(challenge_id) ON DELETE CASCADE,
    media_url TEXT NOT NULL,
    media_type media_type NOT NULL,
    timestamp TIMESTAMP DEFAULT now()
);

-- Recreate the Dares Table
CREATE TABLE dares (
    dare_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    challenge_id UUID REFERENCES challenges(challenge_id) ON DELETE CASCADE,
    dared_to_id UUID REFERENCES users(user_id) ON DELETE CASCADE,
    is_accepted BOOLEAN DEFAULT FALSE,
    is_completed BOOLEAN DEFAULT FALSE,
    completion_date TIMESTAMP,
    completion_rate INTEGER DEFAULT 0
);
