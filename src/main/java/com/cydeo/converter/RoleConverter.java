package com.cydeo.converter;
import com.cydeo.dto.RoleDTO;
import com.cydeo.service.RoleService;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;


@Component
@ConfigurationPropertiesBinding
public class RoleConverter implements Converter<String, RoleDTO> {
    private final RoleService roleService;

    public RoleConverter(@Lazy RoleService roleService) {
        this.roleService = roleService;
    }

    @Override
    public RoleDTO convert(String id) {
        if(id.equals("")) return null;
        return roleService.findById(Long.parseLong(id));
    }
}
