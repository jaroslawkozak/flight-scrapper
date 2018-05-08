package scrapper.wizzair.datamanager.dto;

import lombok.Data;

@Data
public class JobDto {
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
}
