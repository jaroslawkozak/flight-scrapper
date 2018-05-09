package scrapper.wizzair.datamanager.dto;

import lombok.Data;
import lombok.experimental.Accessors;
import scrapper.wizzair.datamanager.enums.JobStatus;

@Data
@Accessors(chain = true)
public class JobReportDto {
    private int jobId;
    private String scrapperName;
    private JobStatus status;
}
