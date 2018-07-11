from flask import Flask
from flask_sqlalchemy import SQLAlchemy
from flask_cors import CORS
from sqlalchemy import Integer, Float, Unicode, ForeignKey, DateTime, Column, or_
from sqlalchemy.orm import relationship

app = Flask('data-manager')
app.config['SQLALCHEMY_DATABASE_URI'] = 'mysql://root:qwe123@10.22.90.79/flight-scrapper'
app.config['SQLALCHEMY_TRACK_MODIFICATIONS'] = False
db = SQLAlchemy(app)
CORS(app)

class Airline(db.Model):
    __tablename__ = 'airlines'
    airlineId = Column('airlineId', Integer, primary_key=True)
    airlineName = Column('airlineName', Unicode)


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


class Currency(db.Model):
    __tablename__ = 'currencyCodes'
    currencyId = Column('currencyId', Integer, primary_key=True)
    currencyCode = Column('currencyCode', Unicode)
    currencyName = Column('currencyName', Unicode)


class Flight(db.Model):
    __tablename__ = 'flights'
    flightId = Column('flightId', Integer, primary_key=True)
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
    hasMacFLight = Column('hasMacFLight', Unicode)
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
    def get_jobs():
        return Job.query\
            .filter(Job.isDeleted == 0)\
            .all()

    @staticmethod
    def get_single_job(jobId):
        return Job.query\
            .filter(Job.jobId == jobId)\
            .first()


