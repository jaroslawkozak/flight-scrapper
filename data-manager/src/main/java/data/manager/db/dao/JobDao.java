package data.manager.db.dao;

import data.manager.db.jdbc.MySqlConnection;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class JobDao extends AbstractDao {
    private final static Logger logger = Logger.getLogger(JobDao.class);
    private int jobId;
    private String departureStationIATA;
    private String arrivalStationIATA;
    private String departureStationId;
    private String arrivalStationId;
    private String status;
    private String lastSuccessfull;
    private String lastFailed;
    private int totalSuccess;
    private int totalFailed;
    private int failedInRow;
    private int isActive;
    
    public JobDao() {}
    public JobDao(ResultSet rs) throws SQLException {
        this
        .setJobId(rs.getInt("jobId"))
        .setDepartureStationIATA(rs.getString("departureStationId")) //TODO parse
        .setArrivalStationIATA(rs.getString("arrivalStationId")) //TODA parse
        .setStatus(rs.getString("status"))
        .setLastSuccessfull(rs.getString("lasySuccessfull"))
        .setLastFailed(rs.getString("lastFailed"))
        .setTotalSuccess(rs.getInt("totalSuccess"))
        .setTotalFailed(rs.getInt("totalFailed"))
        .setFailedInRow(rs.getInt("failedInRow"))
        .setIsActive(rs.getInt("isActive")); 
    }
    
    @Override
    public void insert() {
        StringBuffer insertTableSQL = new StringBuffer()
                .append("INSERT INTO jobs (departureStationId, arrivalStationId) VALUES ")
                .append("(" + this.departureStationId)
                .append(",\"" + this.arrivalStationId + "\"")
                .append(")");                
        executeUpdate(insertTableSQL);
    }

}
