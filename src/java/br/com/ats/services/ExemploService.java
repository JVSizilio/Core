package br.com.ats.services;

import br.com.ats.beans.ExemploBean;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;


@Service("exemploService")
//@Repository
public class ExemploService {
    
    public void addExemplo(ExemploBean exemplo){
        System.out.println("Salvei");
    }
}
