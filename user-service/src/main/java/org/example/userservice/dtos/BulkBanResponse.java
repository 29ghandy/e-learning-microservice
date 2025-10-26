package org.example.userservice.dtos;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class BulkBanResponse {
    private List<BanSuccessDto> banned = new ArrayList<>();
    private List<BanFailedDto> failed = new ArrayList<>();
}
