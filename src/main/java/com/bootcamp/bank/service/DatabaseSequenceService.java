package com.bootcamp.bank.service;

import com.bootcamp.bank.model.DatabaseSequence;
import com.bootcamp.bank.repository.DatabaseSequenceRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Log4j2
@Service
public class DatabaseSequenceService  {

    @Autowired
    DatabaseSequenceRepository databaseSequenceRepository;


    public Mono<Long> generateSequence(String seq) {
        return databaseSequenceRepository.findDatabaseSequenceById(seq).flatMap(sequence -> {
            sequence.setSeq(sequence.getSeq() + 1);
            return databaseSequenceRepository.save(sequence).flatMap(s -> Mono.just(s.getSeq()));
        }).switchIfEmpty(databaseSequenceRepository.save(new DatabaseSequence(seq, 1L))
                .then(Mono.just(1L)));
    }
}