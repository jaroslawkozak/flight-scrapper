from dao import Job, Flight
from utils import timeUtils
from dto import FlightDataDto

MAXIMUM_FLIGHT_DURATION = 30


def get_one_month_data(job_id, from_date):
    job = Job.get_single_job(job_id)
    airports = [job.departureStationId, job.arrivalStationId]
    to_date = timeUtils.add_one_month(from_date)
    flights = Flight.get_flights(airports, airports, from_date, to_date)

    out_flights_dto = []
    in_flights_dto = []

    for flight in flights:
        if ((job.departureAirport.IATA == flight.departureAirport.IATA) and (
                job.arrivalAirport.IATA == flight.arrivalAirport.IATA)):
            out_flights_dto.append(flight)
        elif ((job.departureAirport.IATA == flight.arrivalAirport.IATA) and (
                job.arrivalAirport.IATA == flight.departureAirport.IATA)):
            in_flights_dto.append(flight)
        else:
            print("Error in flight: departure " + flight.departureAirport.IATA
                  + " arrival " + flight.arrivalAirport.IATA)

    flights_dto_response = []
    for outbound in out_flights_dto:
        for inbound in in_flights_dto:
            daysbetween = timeUtils.get_days_between(str(outbound.departureDate), str(inbound.departureDate))
            if (daysbetween > 0) and (daysbetween <= MAXIMUM_FLIGHT_DURATION):
                flights_dto_response.append(FlightDataDto(outbound, inbound).__dict__)

    return flights_dto_response
