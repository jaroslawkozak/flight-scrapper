from flask import Flask
from flask import request
from Responses import ErrorResponse
from utils import timeUtils
from dao import app, Job, Flight
from dto import JobDto, FlightDto
import json
import datetime


def getError(name, message):
    return json.dumps(ErrorResponse(name, message).__dict__)

@app.route("/jobs/add", methods=['POST'])
def add_job():
    departureStation = request.args.get('departureStation')
    arrivalStation = request.args.get('arrivalStation')
    if(not departureStation):
        return getError("Missing parameter", "departureStation is required for that request"), 412
    if(not arrivalStation):
        return getError("Missing parameter", "arrivalStation is required for that request"), 412
    response = "{ /jobs/add : not implemented }"
    return response, 200

@app.route("/jobs/get", methods=['GET'])
def get_jobs():
    jobs = Job.get_jobs()
    jobsDto = []
    for job in jobs:
        jobsDto.append(JobDto(job).__dict__)
    return json.dumps(jobsDto)

@app.route('/jobs/activate', methods=['POST'])
def activate_job():
    jobId = request.args.get('jobId')
    if(not jobId):
        return getError("Missing parameter", "jobId is required for that request")
    response = "{ /jobs/activate : not implemented }"
    return response

@app.route('/jobs/deactivate', methods=['POST'])
def deactivate_job():
    jobId = request.args.get('jobId')
    if(not jobId):
        return getError("Missing parameter", "jobId is required for that request")
    response = "{ /jobs/deactivate : not implemented }"
    return response

@app.route('/jobs/delete', methods=['POST'])
def delete_job():
    jobId = request.args.get('jobId')
    if(not jobId):
        return getError("Missing parameter", "jobId is required for that request")
    response = "{ /jobs/delete : not implemented }"
    return response

@app.route('/flights/getOneMonthData', methods=['GET'])
def get_one_month_data():
    jobId = request.args.get('jobId')
    fromDate = request.args.get('fromDate')
    if(not jobId):
        return getError("Missing parameter", "jobId is required for that request")
    if(not fromDate):
        fromDate = str(datetime.date.today())

    job = Job.get_single_job(jobId)

    airports = [job.departureStationId, job.arrivalStationId]

    toDate = timeUtils.add_one_month(fromDate)

    flights =  Flight.get_flights(airports, airports, fromDate, toDate)

    flightsDto = []

    for flight in flights:

        flightsDto.append(FlightDto(flight).__dict__)
#TODO sortowanie wych i przych, wygenerowac na tej podstawie tablie z FlightDataDto


    return json.dumps(flightsDto)
    #print(fromDate)
    #response = "{ /flights/getOneMonthData : not implemented }"
    #return response

@app.route('/flights/getSingleFlightDetails', methods=['GET'])
def get_single_flight_details():
    flightId = request.args.get('flightId')
    if(not flightId):
        return getError("Missing parameter", "flightId is required for that request")
    response = "{ /flights/getSingleFlightDetails : not implemented }"
    return response

@app.route('/flights/addFlightRecords', methods=['POST'])
def add_flight_records():
    timetable = request.get_json(silent=True)
    scrapperName = request.args.get('scrapperName')
    if(not timetable):
        return getError("Missing parameter", "timetable body is required for that request")
    if (not scrapperName):
        return getError("Missing parameter", "scrapperName is required for that request")
    response = "{ /flights/addFlightRecords : not implemented }"
    return response

@app.route('/jobReport', methods=['POST'])
def job_report():
    jobReport = request.get_json(silent=True)
    if(not jobReport):
        return getError("Missing parameter", "timetable body is required for that request")

    response = "{ /jobReport : not implemented }"
    return response

if __name__ == "__main__":
    app.run(host='0.0.0.0', port=7701, debug=True, threaded=True)



