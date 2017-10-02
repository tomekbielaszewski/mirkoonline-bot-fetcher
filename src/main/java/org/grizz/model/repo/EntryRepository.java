package org.grizz.model.repo;

import org.joda.time.DateTime;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import pl.grizwold.microblog.model.Entry;

import java.util.stream.Stream;

public interface EntryRepository extends MongoRepository<Entry, Long> {

    @Query("{\"dateAdded\": { $gte: ?0}}")
    Stream<Entry> findYoungerThan(DateTime dateAdded);

}
