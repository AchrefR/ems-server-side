package com.ppg.ems_server_side_v0.config.migration;

import com.ppg.ems_server_side_v0.domain.Access;
import com.ppg.ems_server_side_v0.domain.enums.DefaultRole;
import com.ppg.ems_server_side_v0.repository.AccessRepository;
import jakarta.persistence.Entity;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.Filter;
import org.reflections.Reflections;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
@Order(1)
@RequiredArgsConstructor
public class AccessDbMigration implements ApplicationListener<Notifier> {

    private final AccessRepository accessRepository;

    @Override
    public void onApplicationEvent(Notifier event) {

        List<Access> accesses = this.accessRepository.findAll();

        if (accesses.isEmpty()) {
            Reflections reflections = new Reflections("com.ppg.ems_server_side_v0.domain");
            Set<Class<?>> entityClasses = reflections.getTypesAnnotatedWith(Entity.class);

            for (Class<?> entityClass : entityClasses) {
                if (!entityClass.getSimpleName().contains("_")) {
                    Access create = Access.builder().title(entityClass.getSimpleName() + "_CREATE").build();
                    Access update = Access.builder().title(entityClass.getSimpleName() + "_UPDATE").build();
                    Access delete = Access.builder().title(entityClass.getSimpleName() + "_DELETE").build();
                    Access fetch = Access.builder().title(entityClass.getSimpleName() + "_FETCH").build();

                    accessRepository.save(create);
                    accessRepository.save(update);
                    accessRepository.save(delete);
                    accessRepository.save(fetch);
                }
            }

        }

    }
}
