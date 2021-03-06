package data.manager.model.dao;

import data.manager.db.jdbc.MySqlConnection;
import data.manager.enums.JobStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

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
    private int isDeleted;
    
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
        .setIsActive(rs.getInt("isActive"))
        .setIsDeleted(rs.getInt("isDeleted"));
    }
    
    @Override
    public void insert() {
        if(isExistInDB(true)) {
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
        return isExistInDB(false);
    }
    
    private boolean isExistInDB(boolean unDeleteIfPossible) {
        StringBuffer selectQuery = new StringBuffer()
                .append("SELECT * FROM jobs WHERE ")
                .append("departureStationId = \"" + this.departureStationId + "\"")
                .append(" AND ")
                .append("arrivalStationId = \"" + this.arrivalStationId + "\"");   
        try {
            ResultSet rs = MySqlConnection.executeQuery(selectQuery.toString());
            if(rs.first()) {
                JobDao currentJob = new JobDao(rs);
                if(currentJob.getIsDeleted() == 1) {
                    currentJob.undelete();
                }
                return true;
            }
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
                updateQuery.append("status=\"ok\"");
                updateQuery.append(", lastSuccessfull=\"" + nowDate + "\"");
                updateQuery.append(", totalSuccess = totalSuccess + 1");
                updateQuery.append(", failedInRow = 0");
                break;
            case FAILED:
                updateQuery.append("status=\"failing\"");
                updateQuery.append(", lastFailed=\"" + nowDate +"\"");
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
        try {
            logger.trace(updateQuery.toString());
            MySqlConnection.executeUpdate(updateQuery.toString());
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
    }
    
    public void activate() {
        this.toggleActive(true);
    }
    
    public void deactivate() {
        this.toggleActive(false);
    }
    
    private void toggleActive(boolean isActive) {
        if(this.jobId == -1) {
            logger.error("Error during job delete: JobId required to update job");
            return;
        }       
        int active = isActive ? 1 : 0;
        StringBuffer updateQuery = new StringBuffer()
                .append("UPDATE jobs SET isActive=" + active + " WHERE jobId=" + this.jobId);
        try {
            logger.trace(updateQuery.toString());
            MySqlConnection.executeUpdate(updateQuery.toString());
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
    }
    
    public void softDelete() {
        setIsDeleteStatus(true);
    }
    
    public void undelete() {
        setIsDeleteStatus(false);
    }
    
    private void setIsDeleteStatus(boolean isDeleted) {
        if(this.jobId == -1) {
            logger.error("Error during job delete: JobId required to update job");
            return;
        } 
        int deleted = isDeleted ? 1 : 0;
        StringBuffer updateQuery = new StringBuffer()
                .append("UPDATE jobs SET isDeleted=" + deleted + " WHERE jobId=" + this.jobId);
        try {
            logger.trace(updateQuery.toString());
            MySqlConnection.executeUpdate(updateQuery.toString());
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
    }
    
    public static JobDao select(int jobId){       
        System.out.println("here");
        return (JobDao) jdbcTemplate.queryForObject("SELECT * FROM jobs WHERE jobId='?';", new Object[] { jobId }, new BeanPropertyRowMapper(JobDao.class));                      
    }
    
    public static List<JobDao> selectAll(){  
        System.out.println("here2");
        System.out.println(jdbcTemplate);
        return (List<JobDao>) jdbcTemplate.query("SELECT * FROM jobs WHERE isDeleted=0;", new BeanPropertyRowMapper(JobDao.class));
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
