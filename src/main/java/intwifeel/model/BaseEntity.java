package intwifeel.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BaseEntity implements Serializable {

    @Id
    private String id;

    /*
    The map key represents the name of the collection
    The map value represents the IDs to which the current entity is linked
     */
    @JsonIgnore
    protected Map<String, List<String>> references = new HashMap<String, List<String>>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Map<String, List<String>> getReferences() {
        return references;
    }

    public void setReferences(Map<String, List<String>> references) {
        this.references = references;
    }

    protected void addReference(String collection, String referenceId) {
        List<String> referenceIds = new ArrayList<String>();
        referenceIds.add(referenceId);
        references.put(collection, referenceIds);
    }

    protected void addReferenceList(String collection, List<String> referenceList) {
        references.put(collection, referenceList);
    }
}
