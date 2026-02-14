package de.hitohitonika.tcgs.cardcollectionservice.config;

import de.hitohitonika.tcgs.cardcollectionservice.ygo.entities.YgoCard;
import de.hitohitonika.tcgs.cardcollectionservice.ygo.entities.YgoSet;
import org.jspecify.annotations.NonNull;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

@Configuration
public class RestRepositoryConfig implements RepositoryRestConfigurer {

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, @NonNull CorsRegistry cors) {
        HttpMethod[] unsupportedActions = {
                HttpMethod.POST,
                HttpMethod.PUT,
                HttpMethod.PATCH,
                HttpMethod.DELETE
        };

        //Knipst alle schreibenden Aktionen aus den RestRepositories ab

        config.getExposureConfiguration()
                .forDomainType(YgoCard.class)
                .withItemExposure((_, httpMethods) -> httpMethods.disable(unsupportedActions))
                .withCollectionExposure((_, httpMethods) -> httpMethods.disable(unsupportedActions));

        config.getExposureConfiguration()
                .forDomainType(YgoSet.class)
                .withItemExposure((_, httpMethods) -> httpMethods.disable(unsupportedActions))
                .withCollectionExposure((_, httpMethods) -> httpMethods.disable(unsupportedActions));
    }
}
