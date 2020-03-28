import requests

class TestClient:

	def __init__(self):
		self.session = requests.Session()

	""" Make HTTP POST with given JSON data """
	def makePost(self, url, data):
		r = self.session.post(url, json=data)
		self.lastSent = data
		self.lastResponse = r

	""" Make HTTP GET with given JSON data """
	def makeGet(self, url):
		r = self.session.get(url)
		self.lastResponse = r

	""" Print content of last POST JSON object """
	def printLastSend(self):
		print "Sent:\n"
		print self.lastSent
		print "\n"

	""" Print full content of last HTTP response """
	def printLastResponse(self):
		print "(Replied HTTP " + str(self.lastResponse.status_code) + ") Got:\n"
		print self.lastResponse.content
		print "\n"
		
