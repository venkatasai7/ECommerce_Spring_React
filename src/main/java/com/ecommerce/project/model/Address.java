package com.ecommerce.project.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "addresses")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    private Long addressId;

    @NotBlank
    @Size(min = 5, message = "Street name must be atleast 5")
    private String street;


    @Size(min = 5,message = "Building name must be atleast 5" )
    @NotBlank
    private String buildingName;


    @Size(min = 5,message = "City name must be atleast 4" )
    @NotBlank
    private String city;

    @Size(min = 2,message = "State name must be atleast 2" )
    @NotBlank
    private String state;

    @Size(min = 5,message = "Country name must be atleast 2" )
    @NotBlank
    private String country;


    @Size(min = 6,message = "Pincode must be atleast 6" )
    @NotBlank
    private String pincode;


    @ToString.Exclude
    @ManyToMany(mappedBy = "addresses")
    private List<User> users = new ArrayList<>();


    public Address(String street, String buildingName, String city, String state, String country, String pincode) {
        this.street = street;
        this.buildingName = buildingName;
        this.city = city;
        this.state = state;
        this.country = country;
        this.pincode = pincode;
    }
}
