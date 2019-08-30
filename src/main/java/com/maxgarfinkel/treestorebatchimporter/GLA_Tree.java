package com.maxgarfinkel.treestorebatchimporter;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode
public class GLA_Tree {

    @Getter @Setter private String gla_id;
    @Getter @Setter private String borough;
    @Getter @Setter private String species_name;
    @Getter @Setter private String common_name;
    @Getter @Setter private String display_name;
    @Getter @Setter private int load_date;
    @Getter @Setter private int easting;
    @Getter @Setter private int northing;
    @Getter @Setter private double longitude;
    @Getter @Setter private double latitude;

    public GLA_Tree(){
    }

    public GLA_Tree(String gla_id,
            String borough,
            String species_name,
            String common_name,
            String display_name,
            int load_date,
            int easting,
            int northing,
            double longitude,
            double latitude){
        this.gla_id = gla_id;
        this.borough = borough;
        this.species_name = species_name;
        this.common_name = common_name;
        this.display_name = display_name;
        this.load_date = load_date;
        this.easting = easting;
        this.northing = northing;
        this.longitude = longitude;
        this.latitude = latitude;

    }
}
