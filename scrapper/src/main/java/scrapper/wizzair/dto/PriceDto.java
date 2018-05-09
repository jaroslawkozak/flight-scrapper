package scrapper.wizzair.dto;

import lombok.Data;

@Data
public class PriceDto {
    private double amount;
    private String currencyCode;
}
