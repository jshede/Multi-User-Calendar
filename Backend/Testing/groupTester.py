from tester import *

client = TestClient()

# get session cookie
client.makeGet("http://localhost:8080/api/getCurrentUser")

print "Registering...\n"
client.makePost("http://localhost:8080/api/register", {"name" : "Bob Smith", "username" : "bsmith123", "email" : "fooo@oooh.edu", "passwordHash" : "password123"})
client.printLastSend()
client.printLastResponse()

print "\n\Logging in...\n"
client.makePost("http://localhost:8080/api/login", {"username" : "bsmith123", "password" : "password123"})
client.printLastSend()
client.printLastResponse()

print "\n\nCreating group...\n"
client.makePost("http://localhost:8080/api/createGroup", {"id" : 0, "name" : "Gold Team", "description" : "foo"})
client.printLastSend()
client.printLastResponse()
