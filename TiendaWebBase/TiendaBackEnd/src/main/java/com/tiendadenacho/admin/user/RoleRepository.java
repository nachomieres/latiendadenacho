package com.tiendadenacho.admin.user;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.tiendadenacho.entidades.Role;

@Repository
public interface RoleRepository extends CrudRepository<Role, Integer>{

}
