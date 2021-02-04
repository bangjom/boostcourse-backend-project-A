package org.edwith.webbe.cardmanager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class BusinessCard {
    private String name;
    private String phone;
    private String companyName;
    private LocalDateTime createDate;
}
