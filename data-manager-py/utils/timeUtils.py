import datetime
import calendar


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

    return orig_date.replace(year=new_year, month=new_month, day=new_day).strftime('%Y-%m-%d')


def get_days_between(from_date_str, to_date_str):
    try:
        return __get_days_between_ext('%Y-%m-%dT%H:%M:%S', from_date_str, to_date_str)
    except ValueError:
        try:
            return __get_days_between_ext('%Y-%m-%d %H:%M:%S', from_date_str, to_date_str)
        except ValueError:
            raise ValueError


def __get_days_between_ext(format_str, from_date_str, to_date_str):
    from_date = datetime.datetime.strptime(from_date_str, format_str)
    to_date = datetime.datetime.strptime(to_date_str, format_str)
    return (to_date - from_date).days