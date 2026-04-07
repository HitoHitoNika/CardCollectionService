package de.hitohitonika.tcgs.cardcollectionservice.configuration;

import de.hitohitonika.tcgs.cardcollectionservice.data.db.services.TcgServiceHelper;
import de.hitohitonika.tcgs.cardcollectionservice.importers.ImportOrchestrator;
import de.hitohitonika.tcgs.cardcollectionservice.magic.data.services.MagicService;
import de.hitohitonika.tcgs.cardcollectionservice.magic.imports.MagicImporter;
import de.hitohitonika.tcgs.cardcollectionservice.op.data.OpService;
import de.hitohitonika.tcgs.cardcollectionservice.op.imports.OpImporter;
import de.hitohitonika.tcgs.cardcollectionservice.user.db.AppUserService;
import de.hitohitonika.tcgs.cardcollectionservice.user.security.AppUserDetailsService;
import de.hitohitonika.tcgs.cardcollectionservice.ygo.data.YgoService;
import de.hitohitonika.tcgs.cardcollectionservice.ygo.imports.YgoImporter;
import org.jspecify.annotations.NullMarked;
import org.springframework.beans.factory.BeanRegistrar;
import org.springframework.beans.factory.BeanRegistry;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@NullMarked
public class AppBeanRegistrar implements BeanRegistrar {
    @Override
    public void register(BeanRegistry registry, Environment env) {
        registry.registerBean("ygoService", YgoService.class);
        registry.registerBean("magicService", MagicService.class);
        registry.registerBean("opService", OpService.class);
        registry.registerBean("tcgServiceHelper", TcgServiceHelper.class);
        registry.registerBean("appUserService", AppUserService.class);
        registry.registerBean("appUserDetailsService", AppUserDetailsService.class);
        registry.registerBean("ygoImporter", YgoImporter.class);
        registry.registerBean("opImporter", OpImporter.class);
        registry.registerBean("magicImporter", MagicImporter.class);
        registry.registerBean("importOrchestrator", ImportOrchestrator.class);
        registry.registerBean("bCryptPasswordEncoder", BCryptPasswordEncoder.class);
    }
}
