package com.troia.libraryproject.config;

import com.troia.libraryproject.model.user.ERole;
import com.troia.libraryproject.model.user.Role;
import com.troia.libraryproject.repository.user.RoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
public class UserRoleFirstSetupConfig implements ApplicationListener<ApplicationReadyEvent> {

    private final RoleRepository roleRepository;

    public UserRoleFirstSetupConfig(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        List<ERole> roleList = new ArrayList<>(Arrays.asList(ERole.values()));
        for (ERole role : roleList) {
            if (!roleRepository.existsByRole(role)){
                roleRepository.save(new Role(role));
                log.info(role.name() + " saved.");
            }
        }
    }
}
