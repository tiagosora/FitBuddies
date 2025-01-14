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
    userId UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    email TEXT NOT NULL UNIQUE,
    password TEXT NOT NULL,
    firstName TEXT NOT NULL,
    lastName TEXT NOT NULL,
    challengesCompleted INTEGER DEFAULT 0,
    distanceTraveled DOUBLE PRECISION DEFAULT 0.0,
    caloriesBurned INTEGER DEFAULT 0,
    profilePictureUrl TEXT
);

-- Recreate the Friendships Table
CREATE TABLE friendships (
    userId UUID REFERENCES users(userId) ON DELETE CASCADE,
    friendId UUID REFERENCES users(userId) ON DELETE CASCADE,
    isAccepted BOOLEAN DEFAULT FALSE,
    creationDate BIGINT DEFAULT EXTRACT(EPOCH FROM now()),
    PRIMARY KEY (userId, friendId)
);

-- Recreate the Challenges Table
CREATE TABLE challenges (
    challengeId UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    title TEXT NOT NULL,
    description TEXT NOT NULL,
    type TEXT NOT NULL,
    daredById UUID REFERENCES users(userId) ON DELETE CASCADE,
    creationDate BIGINT DEFAULT EXTRACT(EPOCH FROM now()),
    deadlineDate BIGINT,
    goal INTEGER DEFAULT 0,
    pictureUrl TEXT
);

-- Recreate the ChallengeMedia Table
CREATE TABLE challenge_media (
    mediaId UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    challengeId UUID REFERENCES challenges(challengeId) ON DELETE CASCADE,
    mediaUrl TEXT NOT NULL,
    mediaType media_type NOT NULL,
    timestamp BIGINT DEFAULT EXTRACT(EPOCH FROM now())
);

-- Recreate the Dares Table
CREATE TABLE dares (
    dareId UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    challengeId UUID REFERENCES challenges(challengeId) ON DELETE CASCADE,
    daredToId UUID REFERENCES users(userId) ON DELETE CASCADE,
    isAccepted BOOLEAN DEFAULT FALSE,
    isCompleted BOOLEAN DEFAULT FALSE,
    completionDate BIGINT,
    completionRate INTEGER DEFAULT 0
);
