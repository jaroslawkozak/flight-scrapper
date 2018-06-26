import Connection

queryStr = "SELECT * FROM jobs WHERE isDeleted=0"
for output in Connection.MySQLConnection().query(queryStr):
    print(output)