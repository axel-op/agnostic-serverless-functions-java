package fr.axelop.agnosticserverlessfunctions;

import java.util.List;
import java.util.Map;
import java.util.Optional;

interface HttpMessage {

    Optional<String> getBody();

    Map<String, List<String>> getHeaders();

}
