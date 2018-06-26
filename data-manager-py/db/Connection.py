import mysql.connector

class MySQLConnection:
    CONNECTION = None
    CONFIG = {
        'user' : 'root',
        'password' : 'qwe123',
        'host' : '10.22.90.79',
        'database' : 'flight-scrapper',
        'connection_timeout' : 10
    }

    @staticmethod
    def connect():
        if(not MySQLConnection.CONNECTION):
            MySQLConnection.CONNECTION = mysql.connector.connect(**MySQLConnection.CONFIG)
        return MySQLConnection.CONNECTION

    @staticmethod
    def close():
        if (MySQLConnection.CONNECTION):
            MySQLConnection.CONNECTION.close()

    @staticmethod
    def query(myQuery):
        try:
            cursor = MySQLConnection.CONNECTION.cursor()
            cursor.execute(myQuery)
        except (AttributeError, mysql.connector.errors.OperationalError):
            MySQLConnection.connect()
            cursor = MySQLConnection.CONNECTION.cursor()
            cursor.execute(myQuery)
        return cursor
