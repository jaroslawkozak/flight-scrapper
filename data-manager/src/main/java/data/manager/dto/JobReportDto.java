package data.manager.dto;

import data.manager.enums.JobStatus;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class JobReportDto {
    private int jobId;
    private String scrapperName;
    private JobStatus status;
}
