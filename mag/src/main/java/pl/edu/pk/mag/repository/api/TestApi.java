package pl.edu.pk.mag.repository.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/")
public class TestApi {

@GetMapping(path = "/test")
    public ResponseEntity<String> getTest(){
    return new ResponseEntity<>("400", HttpStatus.OK);
}
}
