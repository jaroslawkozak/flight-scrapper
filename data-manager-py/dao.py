from flask import Flask
from flask_sqlalchemy import SQLAlchemy
from flask_cors import CORS
from sqlalchemy import Integer, Float, Unicode, ForeignKey, DateTime, Column, or_
from sqlalchemy.orm import relationship
import datetime

app = Flask('data-manager')
app.config['SQLALCHEMY_DATABASE_URI'] = 'mysql://root:qwe123@10.22.90.79/flight-scrapper'
app.config['SQLALCHEMY_TRACK_MODIFICATIONS'] = False
db = SQLAlchemy(app)
CORS(app)


class Airline(db.Model):
    __tablename__ = 'airlines'
    airlineId = Column('airlineId', Integer, primary_key=True)
    airlineName = Column('airlineName', Unicode)

    @staticmethod
    def get_id_from_name(name):
        return Airline.query.filter(Airline.airlineName == name).first().airlineId

class Airport(db.Model):
    __tablename__ = 'airports'
    airportId = Column('airportId', Integer, primary_key=True)
    name = Column('name', Unicode)
    city = Column('city', Unicode)
    country = Column('country', Unicode)
    IATA = Column('IATA', Unicode)
    ICAO = Column('ICAO', Unicode)
    latitude = Column('latitude', Float)
    longitude = Column('longitude', Float)
    altitude = Column('altitude', Integer)
    timezone = Column('timezone', Float)
    DST = Column('DST', Unicode)

    @staticmethod
    def get_id_from_iata(iata):
        return Airport.get(iata).airportId

    @staticmethod
    def get(iata):
        return Airport.query.filter(Airport.IATA == iata).first()

class Currency(db.Model):
    __tablename__ = 'currencyCodes'
    currencyId = Column('currencyId', Integer, primary_key=True)
    currencyCode = Column('currencyCode', Unicode)
    currencyName = Column('currencyName', Unicode)

    @staticmethod
    def get(currency_id):
        return Currency.query.filter(Currency.currencyId == currency_id).first()


class Flight(db.Model):
    __tablename__ = 'flights'
    flightId = Column('flightId', Integer, primary_key=True, autoincrement=True)
    departureStationId = Column('departureStationId', Integer, ForeignKey('airports.airportId'), primary_key=True)
    departureAirport = relationship("Airport", foreign_keys=[departureStationId])
    arrivalStationId = Column('arrivalStationId', Integer, ForeignKey('airports.airportId'), primary_key=True)
    arrivalAirport = relationship("Airport", foreign_keys=[arrivalStationId])
    departureDate = Column('departureDate', DateTime)
    amount = Column('amount', Float)
    currencyId = Column('currencyId', Integer, ForeignKey('currencyCodes.currencyId'), primary_key=True)
    priceType = Column('priceType', Unicode)
    departureDates = Column('departureDates', Unicode)
    classOfService = Column('classOfService', Unicode)
    hasMacFlight = Column('hasMacFLight', Unicode)
    airlineId = Column('airlineId', Integer, ForeignKey('airlines.airlineId'), primary_key=True)
    airline = relationship("Airline", foreign_keys=[airlineId])
    createdDate = Column('createdDate', DateTime)
    updatedDate = Column('updatedDate', DateTime)

    @staticmethod
    def get_flights(departureStationIds, arrivalStationIds, fromDate, toDate):
        return Flight.query\
                .filter(or_(Flight.departureStationId == v for v in departureStationIds))\
                .filter(or_(Flight.arrivalStationId == v for v in arrivalStationIds))\
                .filter(Flight.departureDate >= fromDate, Flight.departureDate <= toDate)\
                .all()

    @staticmethod
    def get(flight_id):
        return Flight.query.filter(Flight.flightId == flight_id).first()

    @staticmethod
    def add_or_update(flight_dto):
        departure_station_id = Airport.get_id_from_iata(flight_dto['departureStationIATA'])
        arrival_station_id = Airport.get_id_from_iata(flight_dto['arrivalStationIATA'])
        airline_id = Airline.get_id_from_name(flight_dto['airlineName'])
        now_date = datetime.datetime.utcnow()
        tmp_flight = Flight.query.filter(Flight.departureStationId == departure_station_id)\
                    .filter(Flight.arrivalStationId == arrival_station_id)\
                    .filter(Flight.departureDate == flight_dto['departureDate'])\
                    .filter(Flight.airlineId == airline_id).first()
        if tmp_flight is None:
            db.session.add(Flight(departureStationId=departure_station_id,
                   arrivalStationId=arrival_station_id,
                   departureDate=flight_dto['departureDate'],
                   amount=flight_dto['amount'],
                   currencyId=flight_dto['currencyId'],
                   priceType=flight_dto['priceType'],
                   departureDates=unicode(flight_dto['departureDates']),
                   classOfService=flight_dto['classOfService'],
                   hasMacFlight=flight_dto['hasMacFlight'],
                   airlineId=airline_id,
                   createdDate=now_date,
                   updatedDate=now_date))
            db.session.commit()
        else:
            tmp_flight.amount=flight_dto['amount']
            tmp_flight.currencyId=flight_dto['currencyId']
            tmp_flight.priceType=flight_dto['priceType']
            tmp_flight.departureDates=unicode(flight_dto['departureDates'])
            tmp_flight.classOfService=flight_dto['classOfService']
            tmp_flight.hasMacFlight=flight_dto['hasMacFlight']
            tmp_flight.updatedDate=now_date
            db.session.commit()


class Job(db.Model):
    __tablename__ = 'jobs'
    jobId = Column('jobId', Integer, primary_key=True)
    departureStationId = Column('departureStationId', Integer, ForeignKey('airports.airportId'), primary_key=True)
    departureAirport = relationship("Airport", foreign_keys=[departureStationId])
    arrivalStationId = Column('arrivalStationId', Integer, ForeignKey('airports.airportId'), primary_key=True)
    arrivalAirport = relationship("Airport", foreign_keys=[arrivalStationId])
    status = Column('status', Unicode)
    lastSuccessfull = Column('lastSuccessfull', DateTime)
    lastFailed = Column('lastFailed', DateTime)
    totalSuccess = Column('totalSuccess', Integer)
    totalFailed = Column('totalFailed', Integer)
    failedInRow = Column('failedInRow', Integer)
    isActive = Column('isActive', Integer)
    isDeleted = Column('isDeleted', Integer)

    @staticmethod
    def add_job(departure_station_iata, arrival_station_iata):
        departure_station_id = Airport.get_id_from_iata(departure_station_iata);
        arrival_station_id = Airport.get_id_from_iata(arrival_station_iata)

        tmpJob = Job.__is_job_exist(departure_station_id, arrival_station_id)
        if tmpJob:
            if tmpJob.isDeleted == 1:
                tmpJob.isDeleted = 0
                db.session.commit()
                return Job.__get_response_msg("job has been restored")
            else:
                return Job.__get_response_msg("job is already in the system")
        else:
            db.session.add(Job(departureStationId=departure_station_id,
                               arrivalStationId=arrival_station_id,
                               status="new",
                               totalSuccess=0,
                               totalFailed=0,
                               failedInRow=0,
                               isActive=1,
                               isDeleted=0))
            db.session.commit()
            return Job.__get_response_msg("added new job")

    @staticmethod
    def get_jobs():
        return Job.query\
            .filter(Job.isDeleted == 0)\
            .all()

    @staticmethod
    def get_single_job(job_id):
        return Job.query\
            .filter(Job.jobId == job_id).first()

    @staticmethod
    def activate(jobId):
        tmp = Job.get_single_job(jobId)
        if tmp is None:
            return Job.__get_error_msg_no_job(jobId)
        tmp.isActive = 1
        db.session.commit()
        return Job.__get_response_msg("activated job " + jobId)

    @staticmethod
    def deactivate(jobId):
        tmp = Job.get_single_job(jobId)
        if tmp is None:
            return Job.__get_error_msg_no_job(jobId)
        tmp.isActive = 0
        db.session.commit()
        return Job.__get_response_msg("deactivated job " + jobId)

    @staticmethod
    def delete(jobId):

        tmp = Job.get_single_job(jobId)
        if tmp is None:
            return Job.__get_error_msg_no_job(jobId)
        tmp.isDeleted = 1
        db.session.commit()
        return Job.__get_response_msg("deleted job " + jobId)

    @staticmethod
    def undelete(jobId):
        tmp = Job.get_single_job(jobId)
        if tmp is None:
            return Job.__get_error_msg_no_job(jobId)
        tmp.isDeleted = 0
        db.session.commit()
        return Job.__get_response_msg("undeleted job " + jobId)

    @staticmethod
    def updateStatus(job_report):
        tmpJob = Job.get_single_job(job_report['jobId'])
        if tmpJob is None:
            msg = "Error: Job doesn't exist (job id " + str(job_report['jobId']) + ")"
            print msg
            return msg
        status = job_report['status']

        if status == "SUCCESS":
            tmpJob.status = unicode("ok")
            tmpJob.lastSuccessfull = datetime.datetime.now()
            tmpJob.totalSuccess = tmpJob.totalSuccess + 1
            tmpJob.failedInRow = 0
            db.session.commit()
            return "ok"
        elif status == "FAILED":
            tmpJob.status = unicode("failing")
            tmpJob.lastFailed = datetime.datetime.now()
            tmpJob.totalFailed = tmpJob.totalFailed + 1
            tmpJob.failedInRow = tmpJob.failedInRow + 1
            db.session.commit()
            return "ok"
        elif status == "ON_HOLD" or status == "NOT_ACTIVE":
            return "ok"
        else:
            msg = "Error: Incorrect Job status (" + status + ")"
            print msg
            return msg

    @staticmethod
    def __get_error_msg_no_job(jobId):
        return "{ \"error\" : \"no job found with id=" + jobId + "\"}"

    @staticmethod
    def __get_response_msg(response):
        return "{ \"response\" : \"" + response + "\"}"

    @staticmethod
    def __is_job_exist(departure_station_id, arrival_station_id):
        tmpJob = Job.query \
            .filter(Job.departureStationId == departure_station_id) \
            .filter(Job.arrivalStationId == arrival_station_id) \
            .first()
        if tmpJob is None:
            return False
        else:
            return tmpJob
