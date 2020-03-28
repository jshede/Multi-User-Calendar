INSERT INTO user (id, email, name, password_hash, username)
VALUES(1, 'foo@oooh.edu', 'Bob Smith', 'password123', 'bsmith');

INSERT INTO user_group (id, description, name)
VALUES(1, 'very fun stuff', 'Fun');

INSERT INTO event (id, is_high_priority, name)
VALUES (1, true, 'Fun Stuff');

-- Test with:
-- curl -d "eventId=1&parentId=-1&comment=lol&isPrivate=true" -H "Content-Type: application/x-www-form-urlencoded" -X POST http://localhost:8080/api/postEventFeedback
