package com.example.shppyad23.Departure;

import com.example.shppyad23.PostOffice.PostOffice;
import com.example.shppyad23.PostOffice.PostOfficeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class  DepartureService {
    private final DepartureRepository departureRepository;
    private final PostOfficeRepository postOfficeRepository;

    @Autowired
    public DepartureService(DepartureRepository departureRepository, PostOfficeRepository postOfficeRepository) {
        this.departureRepository = departureRepository;
        this.postOfficeRepository = postOfficeRepository;
    }

    public List<Departure> getAllDepartures() {
        log.info("found all departures");
        return departureRepository.findAll();
    }

    public List<Departure> getDeparturesByDate(String date) {
        log.info("found departures by date");
        return departureRepository.findByDate(date);
    }

    public List<Departure> getDeparturesByType(String type) {
        log.info("found departures by type");
        return departureRepository.findByType(type);
    }

    public Departure getDepartureById(Long id) {
        Optional<Departure> departureOptional = departureRepository.findById(id);
        if (departureOptional.isPresent()) {
            log.info("found departure by id");
            return departureOptional.get();
        }
        log.error("departure search failed: invalid id");
        throw new IllegalStateException("no departure with id " + id);
    }

    public Departure addDeparture(Long officeId, Departure departure) {
        Optional<PostOffice> postOfficeOptional = postOfficeRepository.findById(officeId);
        if (postOfficeOptional.isPresent()) {
            PostOffice postOffice = postOfficeOptional.get();
            postOffice.addDeparture(departure);
            departure.setPostOffice(postOffice);

            departureRepository.save(departure);
            postOfficeRepository.save(postOffice);

            log.info("added new departure");
            return departure;
        }

        log.error("departure adding failed: invalid post office id");
        throw new IllegalStateException("no post office with id " + officeId);
    }

    public void deleteDepartureById(Long id) {
        if (departureRepository.existsById(id)) {
            log.info("deleted departure");
            departureRepository.deleteById(id);
            return;
        }

        log.error("departure deleting failed: invalid id");
        throw new IllegalStateException("no departure with id " + id);
    }

}