package br.com.ats.beans;

import org.springframework.stereotype.Component;


@Component(value = "exemploBeanCp")
public class ExemploBean {
 
    private int id;
    private String nome;
    private String email;
    

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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
        
        
}
