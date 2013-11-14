/**
 * @author João Sizílio <joao.sizilio@atsinformatica.com.br>
 * @version 1.0
 * @since 2013-11-07
 */

package br.com.ats.model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.CascadeType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity @Table(name="pessoa")
public class Pessoa implements Serializable {
	
	/**
	 * Classe Pessoa (1)
	 * <p>
	 * As classes beans devem conter apenas os atributos necessários, os métodos getters/setters e o método [2]
	 * toString(), como mostrado nesta classe.
	 * <p>
	 * Faz-se necessário também mapear a classe com as corretas marcações do hibernate;
	 * 
	 */

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
        @Basic(optional = false)
        @Column(name = "id")
	private int id;
	
	@Column(name = "nome")
	private String nome;
	
	@Column(name = "email")
	private String email;
	
        @JoinColumn(name = "fk_endereco", referencedColumnName = "id")
        @ManyToOne(cascade = CascadeType.ALL)
        private Endereco fkEndereco;

        
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getId() {
		return id;
	}
	
        public Endereco getFkEndereco() {
            return fkEndereco;
        }

        public void setFkEndereco(Endereco fkEndereco) {
            this.fkEndereco = fkEndereco;
        }
        
	@Override
	public String toString(){
		return "Exemplo [id=" + id + ", nome=" + nome + ", email="
				+ email  + ", endereco=" + fkEndereco + "]";
	}

}
