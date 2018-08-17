from dto import JobDto
from dao import Job
import json


def get_jobs_json():
    jobs = Job.get_jobs()
    jobs_dto = []
    for job in jobs:
        jobs_dto.append(JobDto(job).__dict__)
    return json.dumps(jobs_dto)


def activate(jobId):
    return Job.activate(jobId)


def deactivate(jobId):
    return Job.deactivate(jobId)


def delete(jobId):
    return Job.delete(jobId)


def undelete(jobId):
    return Job.undelete(jobId)