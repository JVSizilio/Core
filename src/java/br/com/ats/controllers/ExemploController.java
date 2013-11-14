package br.com.ats.controllers;

import br.com.ats.beans.ExemploBean;
import br.com.ats.services.ExemploService;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@RequestMapping("/")
@Controller
public class ExemploController {
    
    @Resource(name = "exemploBeanCp")
    private ExemploBean exemplo;
    @Autowired
    private ExemploService es;
    
    
    @RequestMapping(method = RequestMethod.GET)
    public String PaginaInicial(Model uiModel) { 
        return "index.xhtml";
    }        
    
    @RequestMapping(method = RequestMethod.POST)
    public String addExemplo(){
        es.addExemplo(exemplo);
        return "index.xhtml";
    }
    
    @RequestMapping(params = "prime", method = RequestMethod.GET)
    public String Cadastro(Model uiModel) {
        return "welcomePrimefaces.xhtml";
    }
    

    public ExemploBean getExemplo() {
        return exemplo;
    }

    public void setExemplo(ExemploBean exemplo) {
        this.exemplo = exemplo;
    }
    
    
}
