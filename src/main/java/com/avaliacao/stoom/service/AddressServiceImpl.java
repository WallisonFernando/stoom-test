package com.avaliacao.stoom.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.avaliacao.stoom.exceptions.AddressNotFoundException;
import com.avaliacao.stoom.exceptions.AddressNotFoundOnGoogleAPIException;
import com.avaliacao.stoom.exceptions.MalformedBodyException;
import com.avaliacao.stoom.integration.GoogleAPIIntegration;
import com.avaliacao.stoom.integration.GoogleAPIModel;
import com.avaliacao.stoom.model.Address;
import com.avaliacao.stoom.model.AddressDTO;
import com.avaliacao.stoom.repository.AddressRepository;

@Service
public class AddressServiceImpl implements AddressService {


	@Autowired
    private AddressRepository addressRepository;
    
    @Autowired
    private GoogleAPIIntegration googleAPIIntegration;

    @Override
    public AddressDTO getAddressById(Long id) throws AddressNotFoundException {
        return AddressDTO.fromEntity(addressRepository.findById(id).orElseThrow(AddressNotFoundException::new));
    }

    @Override
    public AddressDTO saveNewAddress(AddressDTO addressDTO) throws AddressNotFoundOnGoogleAPIException {
        if (addressDTO.getLatitude() == null && addressDTO.getLongitude() == null) {
            GoogleAPIModel apiModel = googleAPIIntegration.getLongLatFromAddress(addressDTO);
            if (apiModel.getStatus().equals("ZERO_RESULTS")) {
                throw new AddressNotFoundOnGoogleAPIException();
            }
            addressDTO.setLongitude(apiModel.getResults().get(0).getGeometry().getLocation().getLng());
            addressDTO.setLatitude(apiModel.getResults().get(0).getGeometry().getLocation().getLat());
        }

        Address entity = addressRepository.save(AddressDTO.toEntity(addressDTO));

        return AddressDTO.fromEntity(entity);
    }

    @Override
    public AddressDTO editAddress(AddressDTO addressDTO, Long id) throws MalformedBodyException {
        if (!id.equals(addressDTO.getId())) {
            throw new MalformedBodyException();
        }

        Address entity = addressRepository.save(AddressDTO.toEntity(addressDTO));
        return AddressDTO.fromEntity(entity);
    }

    @Override
    public void deleteAddressById(Long id) throws AddressNotFoundException {
        addressRepository.findById(id).orElseThrow(AddressNotFoundException::new);
        addressRepository.deleteById(id);
    }

	@Override
	public List<AddressDTO> getAllAddress() {
		List<Address> findAllAddress = addressRepository.findAll();
		
		List<AddressDTO> listAddressDTO = findAllAddress
			.stream()
			.map(address ->{
			return AddressDTO.fromEntity(address);
		}).collect(Collectors.toList());
		
		return listAddressDTO;
	}
}
