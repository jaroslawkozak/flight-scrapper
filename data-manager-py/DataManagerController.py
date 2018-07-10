from flask import Flask
from flask import request
from Responses import ErrorResponse
from dao import app, Job, Flight
from dto import JobDto
from dateutil.relativedelta import *
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

    airports = [job.departureAirport.IATA, job.arrivalAirport.IATA]

    toDate = fromDate + relativedelta(months=+1)


    Flight.get_flights(airports, airports, fromDate, toDate)

    print(fromDate)
    response = "{ /flights/getOneMonthData : not implemented }"
    return response

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


def add_one_month(orig_date):
    format_str = '%Y-%m-%d'  # The format
    orig_date = datetime.datetime.strptime(orig_date, format_str)
    # advance year and month by one month
    new_year = orig_date.year
    new_month = orig_date.month + 1
    # note: in datetime.date, months go from 1 to 12
    if new_month > 12:
        new_year += 1
        new_month -= 12

    last_day_of_month = calendar.monthrange(new_year, new_month)[1]
    new_day = min(orig_date.day, last_day_of_month)

    return str(orig_date.replace(year=new_year, month=new_month, day=new_day))

if __name__ == "__main__":
    app.run(host='0.0.0.0', port=7701, debug=True, threaded=True)



