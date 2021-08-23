package com.avaliacao.stoom.rest;

import com.avaliacao.stoom.exceptions.AddressNotFoundException;
import com.avaliacao.stoom.exceptions.MalformedBodyException;
import com.avaliacao.stoom.model.AddressDTO;
import com.avaliacao.stoom.service.AddressService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/addresses")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<AddressDTO>> getAllAddress() {
        
        List<AddressDTO> allAddress = addressService.getAllAddress();
        if(allAddress.isEmpty()) {
        	return ResponseEntity.status(HttpStatus.NO_CONTENT).body(allAddress);
        }
		return ResponseEntity.status(HttpStatus.OK).body(allAddress);
    }


    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AddressDTO> getAddressById(@PathVariable(value = "id", name = "id") Long id) {
        try {
            AddressDTO addressById = addressService.getAddressById(id);
			return ResponseEntity.status(HttpStatus.OK).body(addressById);
        } catch (AddressNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }


    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AddressDTO> saveNewAddress(@Valid @RequestBody AddressDTO addressDTO) {
        try {
            AddressDTO saveNewAddress = addressService.saveNewAddress(addressDTO);
			return ResponseEntity.status(HttpStatus.CREATED).body(saveNewAddress);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AddressDTO> saveNewAddress(@Valid @RequestBody AddressDTO addressDTO, @PathVariable(value = "id", name = "id") Long id) {
        try {
        	AddressDTO editAddress = addressService.editAddress(addressDTO, id);
            return ResponseEntity.status(HttpStatus.OK).body(editAddress);
        } catch (MalformedBodyException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }


    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteAddressById(@PathVariable(value = "id", name = "id") Long id) {
        try {
            addressService.deleteAddressById(id);
            return ResponseEntity.ok().build();
        } catch (AddressNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

}
