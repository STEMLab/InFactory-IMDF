package edu.pnu.stem.geojson;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class Solid extends Geometry {
    private final double[][][][][] coordinates;
    private final double[] bbox;

    @JsonCreator
    public Solid(@JsonProperty("coordinates") double [][][][][] coordinates) {
        super();
        this.coordinates = coordinates;
        this.bbox = null;
    }

    public double[][][][][] getCoordinates() {
        return coordinates;
    }

    public double[] getBbox() {
        return bbox;
    }
}