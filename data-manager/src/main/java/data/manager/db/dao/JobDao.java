package data.manager.db.dao;

import data.manager.db.jdbc.MySqlConnection;
import data.manager.enums.JobStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class JobDao extends AbstractDao {
    private final static Logger logger = Logger.getLogger(JobDao.class);
    private int jobId = -1;
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
        //.setDepartureStationIATA(AirportDao.select(rs.getString("departureStationId")).getIATA()) //Done in ID setter
        .setDepartureStationId(rs.getString("departureStationId"))
        //.setArrivalStationIATA(AirportDao.select(rs.getString("arrivalStationId")).getIATA()) //Done in ID setter
        .setArrivalStationId(rs.getString("arrivalStationId"))
        .setStatus(rs.getString("status"))
        .setLastSuccessfull(rs.getString("lastSuccessfull"))
        .setLastFailed(rs.getString("lastFailed"))
        .setTotalSuccess(rs.getInt("totalSuccess"))
        .setTotalFailed(rs.getInt("totalFailed"))
        .setFailedInRow(rs.getInt("failedInRow"))
        .setIsActive(rs.getInt("isActive")); 
    }
    
    @Override
    public void insert() {
        if(isExistInDB()) {
            logger.debug("Job with departureStationId=" + this.departureStationId + " and arrivalStationId=" + this.arrivalStationId + " already exists in database.");
            return;
        }
        StringBuffer insertTableSQL = new StringBuffer()
                .append("INSERT INTO jobs (departureStationId, arrivalStationId) VALUES ")
                .append("(" + this.departureStationId)
                .append(",\"" + this.arrivalStationId + "\"")
                .append(")");                
        executeUpdate(insertTableSQL);
    }
    
    private boolean isExistInDB() {
        StringBuffer selectQuery = new StringBuffer()
                .append("SELECT * FROM jobs WHERE ")
                .append("departureStationId = \"" + this.departureStationId + "\"")
                .append(" AND ")
                .append("arrivalStationId = \"" + this.arrivalStationId + "\"");   
        try {
            return MySqlConnection.executeQuery(selectQuery.toString()).first();
        } catch (SQLException e) {            
            logger.error(e.getMessage());
        }
        return false;
    }
    
    public void update(JobStatus status) {
        if(this.jobId == -1) {
            logger.error("Error during job update: JobId required to update job");
            return;
        }
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        String nowDate = sdf.format(new Date());
        
        StringBuffer updateQuery = new StringBuffer()
                .append("UPDATE jobs SET ");
        switch(status) {
            case SUCCESS:
                updateQuery.append("status=ok");
                updateQuery.append(", lastSuccessfull=\"" + nowDate + "\"");
                updateQuery.append(", totalSuccess = totalSuccess + 1");
                updateQuery.append(", failedInRow = 0");
                break;
            case FAILED:
                updateQuery.append("status=\"failing\"");
                updateQuery.append(", lastFailed=" + nowDate);
                updateQuery.append(", totalFailed = totalFailed + 1");
                updateQuery.append(", failedInRow = failedInRow + 1");
                break;
            case ON_HOLD:
                break;
            case NOT_ACTIVE:        
                break;
            default:
                logger.error("Incorrect Job status (" + status + ")");
                return;             
        }
        updateQuery.append(" WHERE jobId=" + this.jobId);
        
    }
    
    public static JobDao select(int jobId){
        StringBuffer selectJobsQuery = new StringBuffer()
                .append("SELECT * FROM jobs WHERE jobId='" + jobId + "';");  
      
        try {
            ResultSet rs = MySqlConnection.executeQuery(selectJobsQuery.toString());
            rs.first();
            return new JobDao(rs); 
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
        return null;                        
    }
    
    public static List<JobDao> selectAll(){
        List<JobDao> responseJobs = new ArrayList<JobDao>();
        StringBuffer selectJobsQuery = new StringBuffer()
                .append("SELECT * FROM jobs;");  
        try {
            ResultSet rs = MySqlConnection.executeQuery(selectJobsQuery.toString());
            while(rs.next()) {
                responseJobs.add(new JobDao(rs));                    
            }           
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
        return responseJobs;
    }

    public JobDao setDepartureStationIATA(String departureStationIATA) {
        this.departureStationIATA = departureStationIATA;
        this.departureStationId = Integer.toString(AirportDao.select(departureStationIATA).getAirportId());
        return this;
    }
    
    public JobDao setArrivalStationIATA(String arrivalStationIATA) {
        this.arrivalStationIATA = arrivalStationIATA;
        this.arrivalStationId = Integer.toString(AirportDao.select(arrivalStationIATA).getAirportId());
        return this;
    }
    
    public JobDao setDepartureStationId(String departureStationId) {
        this.departureStationId = departureStationId;
        this.departureStationIATA = AirportDao.select(Integer.parseInt(departureStationId)).getIATA();
        return this;
    }
    
    public JobDao setArrivalStationId(String arrivalStationId) {
        this.arrivalStationId = arrivalStationId;
        this.arrivalStationIATA = AirportDao.select(Integer.parseInt(arrivalStationId)).getIATA();
        return this;
    }
}
