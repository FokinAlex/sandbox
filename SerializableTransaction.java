import java.util.UUID;

public class SerializableTransaction {

    private static final String REJECT = "REJECT";

    private final Repository<UUID, Document> repository;

    public SerializableTransaction(Repository repository) {
        this.repository = repository;
    }

    // @Transactional(isolation = Isolation.SERIALIZABLE)
    public Document create(UUID otherDocumentId) {
        boolean exists = repository.existsByOtherDocumentIdAndStatusIsNot(otherDocumentId, REJECT);
        if (exists) {
            throw new RuntimeException("Already exists");
        }

        // Create new document:
        Document document = new Document() {};
        // Fill some fields...
        // And other document id too

        UUID documentId = repository.save(document);

        return repository.findById(documentId);
    }

}

interface Document {

    default UUID getId() {
        return UUID.randomUUID();
    }
}

interface Repository<Id, Document> {

    boolean existsByOtherDocumentIdAndStatusIsNot(UUID otherDocumentId, String status);

    Document findById(Id id);

    Id save(Document document);
}
