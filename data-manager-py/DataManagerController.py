from flask import Flask
from flask import request
from Responses import ErrorResponse
from db import Connection
import json

app = Flask(__name__)

class TestClass:
    def __init__(self):
        self.abs = 123
        self.bsd = 2131

    def getsome(self):
        return self.abs

def getError(name, message):
    return json.dumps(ErrorResponse(name, message).__dict__)

@app.route("/jobs/add")
def addJob():
    departureStation = request.args.get('departureStation')
    arrivalStation = request.args.get('arrivalStation')
    if(not departureStation):
        return getError("Missing parameter", "departureStation is required for that request"), 412
    if(not arrivalStation):
        return getError("Missing parameter", "arrivalStation is required for that request"), 412
    response = "{ departureStation : " + departureStation + ", arrivalStation : " + arrivalStation + " }"
    return response, 200

@app.route("/jobs/get")
def addJob():
    response = Connection.MySQLConnection().query("SELECT * FROM jobs WHERE isDeleted=0;")
    return response

@app.route('/<username>')
def show_user_profile(username):
    # show the user profile for that user
    param = request.args.get('para')
    return 'User %s, param %s' % (username, param)

@app.route('/about/')
def about():
    return json.dumps(TestClass().__dict__)

if __name__ == "__main__":
    for key in TestClass().__dict__:
        print('key ' + key)
    app.run(debug=True, threaded=True)


