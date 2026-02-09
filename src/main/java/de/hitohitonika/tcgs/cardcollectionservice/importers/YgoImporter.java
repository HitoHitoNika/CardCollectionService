package de.hitohitonika.tcgs.cardcollectionservice.importers;

import de.hitohitonika.tcgs.cardcollectionservice.ygo.services.YgoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBooleanProperty;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@ConditionalOnBooleanProperty(
        prefix = "app.imports.ygo",
        name = "enabled"
)
public class YgoImporter implements DataImporter {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final RestClient restClient;

    private final YgoService ygoService;

    YgoImporter(@Value("${app.imports.ygo.address}") String address, YgoService ygoService) {
        restClient = RestClient.builder()
                .baseUrl(address)
                .build();

        this.ygoService = ygoService;
    }

    @Override
    public void importData() throws ImportException {
        log.info("Importing YGO data...");

        var ygoImportData = restClient.get().retrieve().body(new ParameterizedTypeReference<YgoImportData>() {});

        if(ygoImportData == null) {
            throw new ImportException("No YGO data found");
        }

        ygoService.importData(ygoImportData);
    }

    @Override
    public boolean supports(DataTypes category) {
        return category == DataTypes.YGO;
    }
}
