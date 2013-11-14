package br.com.ats.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@RequestMapping("/")
@Controller
public class HomeController {
    
    @RequestMapping(method = RequestMethod.GET)
    public String PaginaInicial(Model uiModel) {
        uiModel.addAttribute("nomeVariavel", "Mensagem enviada pelo controller!");        
        return "index.xhtml";
    }  
    
   
}
