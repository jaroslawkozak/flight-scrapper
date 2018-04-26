package scrapper.wizzair.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class PriceDto {
    private double amount;
    private String currencyCode;
}
