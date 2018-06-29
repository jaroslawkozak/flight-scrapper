from flask import Flask
from flask_sqlalchemy import SQLAlchemy

app = Flask(__name__)
app.config['SQLALCHEMY_DATABASE_URI'] = 'mysql://root:qwe123@10.22.90.79/flight-scrapper'
db = SQLAlchemy(app)

class Job(db.Model):
    __tablename__ = 'jobs'
    jobId = db.Column('jobId', db.Integer, primary_key=True)
    #departureStationIATA = None
    #arrivalStationIATA = None
    departureStationId = db.Column('departureStationId', db.Integer)
    arrivalStationId = db.Column('arrivalStationId', db.Integer)
    status = db.Column('status', db.Unicode)
    lastSuccessfull = db.Column('lastSuccessfull', db.DateTime)
    lastFailed = db.Column('lastFailed', db.DateTime)
    totalSuccess = db.Column('totalSuccess', db.Integer)
    totalFailed = db.Column('totalFailed', db.Integer)
    failedInRow = db.Column('failedInRow', db.Integer)
    isActive = db.Column('isActive', db.Integer)
    isDeleted = db.Column('isDeleted', db.Integer)

jobs = Job.query.all()
print(jobs)
    # def __init__(self, **args):
    #     if args.get('jobId'): self.jobId = args.get('jobId')
    #     else: self.jobId = None
    #     if args.get('departureStationIATA'): self.departureStationIATA = args.get('departureStationIATA')
    #     else: self.departureStationIATA = None
    #     if args.get('arrivalStationIATA'): self.arrivalStationIATA = args.get('arrivalStationIATA')
    #     else: self.arrivalStationIATA = None
    #     if args.get('departureStationId'): self.departureStationId = args.get('departureStationId')
    #     else: self.departureStationId = None
    #     if args.get('arrivalStationId'): self.arrivalStationId = args.get('arrivalStationId')
    #     else: self.arrivalStationId = None
    #     if args.get('status'): self.status = args.get('status')
    #     else: self.status = None
    #     if args.get('lastSuccessfull'): self.lastSuccessfull = args.get('lastSuccessfull')
    #     else: self.lastSuccessfull = None
    #     if args.get('lastFailed'): self.lastFailed = args.get('lastFailed')
    #     else: self.lastFailed = None
    #     if args.get('totalSuccess'): self.totalSuccess = args.get('totalSuccess')
    #     else: self.totalSuccess = None
    #     if args.get('totalFailed'): self.totalFailed = args.get('totalFailed')
    #     else: self.totalFailed = None
    #     if args.get('failedInRow'): self.failedInRow = args.get('failedInRow')
    #     else: self.failedInRow = None
    #     if args.get('isActive'): self.isActive = args.get('isActive')
    #     else: self.isActive = None
    #     if args.get('isDeleted'): self.isDeleted = args.get('isDeleted')
    #     else: self.isDeleted = None
