package school.hei.haapi.service;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import school.hei.haapi.model.Place;
import school.hei.haapi.repository.PlaceRepository;

import java.util.List;

@Service
@AllArgsConstructor

public class PlaceService {
  private PlaceRepository placeRepository ;

  public List<Place> getPlaces(){
    return placeRepository.findAll() ;
  }

  public Place createOrUpdatePlaces(Place place){
    return placeRepository.save(place) ;
  }

  public Place getPlacesById(String id){
    return placeRepository.findById(id).get() ;
  }

}
