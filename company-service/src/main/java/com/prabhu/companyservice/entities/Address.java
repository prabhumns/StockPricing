package com.prabhu.companyservice.entities;

import javax.persistence.Embeddable;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @project company-service
 * @authoer Prabhu Madipalli
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class Address {

    private String addressLine1;

    private String addressLine2;

    private String addressLine3;

    private String city;

    @Size(max = 6, min = 5)
    private String pinCode;
}
