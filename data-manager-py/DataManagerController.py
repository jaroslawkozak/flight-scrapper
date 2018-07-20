from flask import request, Response
from Responses import ErrorResponse
from services import FlightService, JobService
from dao import app
import json
import datetime


def get_error(name, message):
    return json.dumps(ErrorResponse(name, message).__dict__)


@app.route("/jobs/add", methods=['POST'])
def add_job():
    departure_station = request.args.get('departureStation')
    arrival_station = request.args.get('arrivalStation')
    if not departure_station :
        return get_error("Missing parameter", "departureStation is required for that request"), 412
    if not arrival_station:
        return get_error("Missing parameter", "arrivalStation is required for that request"), 412
    json_message = "{ /jobs/add : not implemented }"
    return Response(json_message, status=200, mimetype='application/json')


@app.route("/jobs/get", methods=['GET'])
def get_jobs():
    json_message = JobService.get_jobs_json()
    return Response(json_message, status=200, mimetype='application/json')


@app.route('/jobs/activate', methods=['POST'])
def activate_job():
    job_id = request.args.get('jobId')
    if not job_id :
        return get_error("Missing parameter", "jobId is required for that request")
    json_message = "{ /jobs/activate : not implemented }"
    return Response(json_message, status=200, mimetype='application/json')


@app.route('/jobs/deactivate', methods=['POST'])
def deactivate_job():
    job_id = request.args.get('jobId')
    if not job_id :
        return get_error("Missing parameter", "jobId is required for that request")
    json_message = "{ /jobs/deactivate : not implemented }"
    return Response(json_message, status=200, mimetype='application/json')


@app.route('/jobs/delete', methods=['POST'])
def delete_job():
    job_id = request.args.get('jobId')
    if not job_id :
        return get_error("Missing parameter", "jobId is required for that request")
    json_message = "{ /jobs/delete : not implemented }"
    return Response(json_message, status=200, mimetype='application/json')


@app.route('/flights/getOneMonthData', methods=['GET'])
def get_one_month_data():
    job_id = request.args.get('jobId')
    from_date = request.args.get('fromDate')
    if not job_id:
        return get_error("Missing parameter", "jobId is required for that request")
    if not from_date:
        from_date = str(datetime.date.today())
    json_message = json.dumps(FlightService.get_one_month_data(job_id, from_date))
    return Response(json_message, status=200, mimetype='application/json')


@app.route('/flights/getSingleFlightDetails', methods=['GET'])
def get_single_flight_details():
    flight_id = request.args.get('flightId')
    if not flight_id:
        return get_error("Missing parameter", "flightId is required for that request")
    json_message = "{ /flights/getSingleFlightDetails : not implemented }"
    return Response(json_message, status=200, mimetype='application/json')


@app.route('/flights/addFlightRecords', methods=['POST'])
def add_flight_records():
    timetable = request.get_json(silent=True)
    scrapper_name = request.args.get('scrapperName')
    if not timetable:
        return get_error("Missing parameter", "timetable body is required for that request")
    if not scrapper_name:
        return get_error("Missing parameter", "scrapperName is required for that request")
    json_message = "{ /flights/addFlightRecords : not implemented }"
    return Response(json_message, status=200, mimetype='application/json')


@app.route('/jobReport', methods=['POST'])
def job_report():
    job_report_data = request.get_json(silent=True)
    if not job_report_data:
        return get_error("Missing parameter", "timetable body is required for that request")
    json_message = "{ /jobReport : not implemented }"
    return Response(json_message, status=200, mimetype='application/json')


if __name__ == "__main__":
    app.run(host='0.0.0.0', port=7701, debug=True, threaded=True)



