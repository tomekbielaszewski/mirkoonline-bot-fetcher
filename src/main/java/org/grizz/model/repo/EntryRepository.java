package org.grizz.model.repo;

import org.springframework.data.mongodb.repository.MongoRepository;
import pl.grizwold.microblog.model.Entry;

public interface EntryRepository extends MongoRepository<Entry, Long> {
}
