package org.apache.fineract.data.models.geolocation

import com.google.android.gms.maps.model.Polyline
import com.google.maps.model.DirectionsLeg

/**
 * Created by Ahmad Jawid Muhammadi on 22/7/20.
 */

data class PolylineData(
        var polyline: Polyline,
        var leg: DirectionsLeg
)