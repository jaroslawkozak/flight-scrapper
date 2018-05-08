package data.manager.dto;

import data.manager.enums.JobStatus;
import lombok.Data;

@Data
public class JobReportDto {
    private int jobId;
    private String scrapperName;
    private JobStatus status;
}
