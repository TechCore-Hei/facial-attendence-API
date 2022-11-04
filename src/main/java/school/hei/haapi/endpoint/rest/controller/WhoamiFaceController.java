package school.hei.haapi.endpoint.rest.controller;

import com.amazonaws.services.rekognition.model.CompareFacesResult;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import school.hei.haapi.endpoint.rest.model.Whoami;
import school.hei.haapi.endpoint.rest.security.model.Principal;
import school.hei.haapi.service.CompareFaceService;

import java.io.IOException;
import java.rmi.RemoteException;

@AllArgsConstructor
@RestController
@Controller
@EqualsAndHashCode
@RequestMapping("whoamiFace")
public class WhoamiFaceController {

  @GetMapping
  public Whoami whoamiFace(@AuthenticationPrincipal Principal principal , @RequestBody MultipartFile multipartFile) throws IOException {

    Whoami whoami = new Whoami();
    whoami.setId(principal.getUserId());
    whoami.setBearer(principal.getBearer());
    whoami.setRole(Whoami.RoleEnum.valueOf(principal.getRole()));
    CompareFacesResult compareFacesResult = new CompareFacesResult();
    CompareFaceService compareFaceService = null;

    if(compareFaceService.compareFaces(multipartFile.getBytes() , principal.getPicture()) == compareFacesResult.getFaceMatches().stream().map(this::canEqual)){
      return whoami;
    }
    else{
      throw  new RemoteException();
    }
  }
}
