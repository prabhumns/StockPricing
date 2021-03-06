package com.prabhu.companyservice.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

/**
 * @project company-service
 * @authoer Prabhu Madipalli
 */

@NoArgsConstructor
@Data
@AllArgsConstructor
@Embeddable
public class CompanyDetails {

    private String description;

    private String services;

    private String products;

}
