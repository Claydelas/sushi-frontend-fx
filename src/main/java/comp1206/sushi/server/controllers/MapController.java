package comp1206.sushi.server.controllers;

import com.lynden.gmapsfx.GoogleMapView;
import com.lynden.gmapsfx.MapComponentInitializedListener;
import com.lynden.gmapsfx.javascript.object.*;
import comp1206.sushi.common.Postcode;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;

import static com.lynden.gmapsfx.javascript.object.MapTypeIdEnum.ROADMAP;

public class MapController extends MainViewController implements MapComponentInitializedListener {

    @FXML
    GoogleMapView mapView;
    GoogleMap map;
    ObservableList<Postcode> postcodes;

    @FXML
    public void initialize() {
        mapView.addMapInializedListener(this);
    }

    @Override
    public void mapInitialized() {
        MapOptions mapOptions = new MapOptions();

        mapOptions.center(new LatLong(server.getRestaurantPostcode().getLatLong().get("lat"), server.getRestaurantPostcode().getLatLong().get("long")))
                .mapType(ROADMAP)
                .overviewMapControl(false)
                .panControl(false)
                .rotateControl(false)
                .scaleControl(false)
                .streetViewControl(false)
                .zoomControl(false)
                .zoom(15);

        map = mapView.createMap(mapOptions);
        newMarkers();

        postcodes = FXCollections.observableList(server.getPostcodes());
        postcodes.addListener((ListChangeListener<? super Postcode>) change -> {
            map.clearMarkers();
            for (Postcode p : server.getPostcodes()) {
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(new LatLong(p.getLatLong().get("lat"),
                        p.getLatLong().get("long")))
                        .visible(true)
                        .title(p.getName());

                map.addMarker(new Marker(markerOptions));
            }
        });
    }

    public void newMarkers() {
        for (Postcode p : server.getPostcodes()) {
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(new LatLong(p.getLatLong().get("lat"),
                    p.getLatLong().get("long")))
                    .visible(true)
                    .title(p.getName());

            map.addMarker(new Marker(markerOptions));
        }
    }
}
