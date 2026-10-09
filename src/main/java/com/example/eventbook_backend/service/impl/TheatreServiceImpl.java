package com.example.eventbook_backend.service.impl;

import com.example.eventbook_backend.dto.theatre.TheatreRequest;
import com.example.eventbook_backend.dto.theatre.TheatreResponse;
import com.example.eventbook_backend.entity.Theatre;
import com.example.eventbook_backend.exception.ResourceNotFoundException;
import com.example.eventbook_backend.mapper.TheatreMapper;
import com.example.eventbook_backend.repository.TheatreRepository;
import com.example.eventbook_backend.service.TheatreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TheatreServiceImpl implements TheatreService {

    private final TheatreRepository theatreRepository;
    private final TheatreMapper theatreMapper;

    @Override
    public TheatreResponse createTheatre(TheatreRequest request) {

        if(theatreRepository.existsByName(request.getName())){
            throw new IllegalStateException("Theatre name already exist");
        }

        Theatre theatre = Theatre.builder()
                .name(request.getName())
                .address(request.getAddress())
                .city(request.getCity())
                .state(request.getState())
                .pincode(request.getPincode())
                .imageUrl(request.getImageUrl())
                .build();

        Theatre savedTheatre = theatreRepository.save(theatre);

        return theatreMapper.toResponse(savedTheatre);
    }

    @Override
    public TheatreResponse getTheatreById(Long id) {

        Theatre theatre = theatreRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Theatre not found"));

        return theatreMapper.toResponse(theatre);
    }

    @Override
    public List<TheatreResponse> getAllTheatres() {
        return theatreRepository.findAll()
                .stream()
                .map(theatreMapper::toResponse)
                .toList();
    }

    @Override
    public TheatreResponse updateTheatre(Long id, TheatreRequest request) {

        if(theatreRepository.existsByNameAndIdNot(request.getName(), id)){
            throw new IllegalStateException("Theatre name already exist");
        }

        Theatre theatre = theatreRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Theatre not found"));

        theatre.setName(request.getName());
        theatre.setAddress(request.getAddress());
        theatre.setCity(request.getCity());
        theatre.setState(request.getState());
        theatre.setPincode(request.getPincode());
        theatre.setImageUrl(request.getImageUrl());

        Theatre updatedTheatre = theatreRepository.save(theatre);

        return theatreMapper.toResponse(updatedTheatre);
    }

    @Override
    public void deleteTheatre(Long id) {

        Theatre theatre = theatreRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Theatre not found"));

        theatreRepository.delete(theatre);
    }
}
