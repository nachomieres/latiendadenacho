package com.tiendadenacho.entidades;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table (name = "users")
public class User {
	
	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column (length = 128, nullable = false, unique = true)
	private String email;
	
	@Column (length = 64, nullable = false)
	private String password;
	
	@Column (name = "nombre", length = 45, nullable = false)
	private String nombre;
	
	@Column (name = "apellidos", length = 45, nullable = false)
	private String apellidos;
		
	@Column (length = 64)
	private String foto;
	
	private boolean enabled;
	
	@ManyToMany (fetch = FetchType.EAGER)
	@JoinTable (
			name = "users_roles",
			joinColumns = @JoinColumn (name = "user_id"),
			inverseJoinColumns = @JoinColumn (name = "role_id")
			
			)
	private Set<Role> roles = new HashSet<>();
	
	public User() {

	}
	
	public User(String email, String password, String nombre, String apellidos) {
		super();
		this.email = email;
		this.password = password;
		this.nombre = nombre;
		this.apellidos = apellidos;
	}
	
	

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public String getFoto() {
		return foto;
	}

	public void setFoto(String foto) {
		this.foto = foto;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}
	
	public void addRole (Role role) {
		this.roles.add(role);
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", email=" + email + ", nombre=" + nombre + ", apellidos=" + apellidos + ", activo="
				+ enabled + ", roles=" + roles + "]";
	}
	
	@Transient
	public String getFotoImagePath() {
		if (id == null || foto == null) return "/images/default-user.png";
		
		return "/user-photos/" + this.id + "/" + this.foto;
	}
	
	@Transient
	public String getNombreCompleto() {
		return nombre + " " + apellidos;
	}
	
	public boolean hasRole(String roleName) {
		Iterator<Role> iterator = roles.iterator();
		
		while (iterator.hasNext()) {
			Role role = iterator.next();
			if (role.getName().equals(roleName)) {
				return true;
			}
		}
		
		return false;
	}
	
	
	

}
