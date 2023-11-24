package com.dhbw.unilife.rnv.network;

import com.dhbw.unilife.rnv.network.classes.Parameters;
import com.dhbw.unilife.rnv.network.classes.Trip;

import java.util.List;

public class ModelRnvDepartures {
   Parameters[] parameters;
   String statusText;
   Integer code;

   List<Trip> trips;

   public List<Trip> getTrips() {
      return trips;
   }


   public String getStatusText() {
      return statusText;
   }

   public Integer getCode() {
      return code;
   }

   public Parameters[] getParameters() {
      return parameters;
   }
}

