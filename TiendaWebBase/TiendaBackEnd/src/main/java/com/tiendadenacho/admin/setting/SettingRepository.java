package com.tiendadenacho.admin.setting;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.tiendadenacho.entidades.Setting;
import com.tiendadenacho.entidades.SettingCategory;

public interface SettingRepository extends CrudRepository<Setting, String> {
	
	public List<Setting> findByCategory(SettingCategory category);

}
