package br.com.ats.service;

import br.com.ats.hibernate.AbstractHibernateDAO;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import br.com.ats.model.Pessoa;


/**
 *
 * @author NiwKleber
 */
@Service("documentoService")
@Repository
public class PessoaService  extends AbstractHibernateDAO<Pessoa> {
     
    public PessoaService() {
        super();
    }
}



