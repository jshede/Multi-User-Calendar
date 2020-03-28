from tester import *

client = TestClient()

# get session cookie
client.makeGet("http://localhost:8080/api/getCurrentUser")

print "Testing register\n"
client.makePost("http://localhost:8080/api/register", {"name" : "Bob Smith", "username" : "bsmith123", "email" : "fooo@oooh.edu", "passwordHash" : "password123"})
client.printLastSend()
client.printLastResponse()
