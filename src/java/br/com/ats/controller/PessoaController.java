package br.com.ats.controller;

import br.com.ats.model.Pessoa;
import br.com.ats.service.PessoaService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RequestMapping("/pessoa")
@Controller
public class PessoaController {

    @Autowired
    PessoaService ps;

    @RequestMapping(method = RequestMethod.GET)
    public String Consulta(Model uiModel) {
        List<Pessoa> lista = ps.listAll();
        uiModel.addAttribute("lista", lista);

        return "pessoa/consulta";
    }

    @RequestMapping(params = "cadastro", method = RequestMethod.GET)
    public String Cadastro(Model uiModel) {
        uiModel.addAttribute("pessoa", new Pessoa());
        return "pessoa/cadastro";
    }

    @RequestMapping(params = "cadastro", method = RequestMethod.POST)
    public String Salvar(Pessoa pessoa, Model uiModel, RedirectAttributes redirectAttributes) {
        uiModel.addAttribute("pessoa", new Pessoa());

        if (pessoa.getId() == null) {
            redirectAttributes.addFlashAttribute("mensagem", "Pessoa salva com sucesso!");
            ps.save(pessoa);
        } else {
            redirectAttributes.addFlashAttribute("mensagem", "Pessoa atualizada com sucesso!");
            ps.update(pessoa);
        }

        return "redirect:/pessoa";
    }

    @RequestMapping(value = "{id}", params = "editar", method = RequestMethod.GET)
    public String Editar(@PathVariable Integer id, Model uiModel, RedirectAttributes redirectAttributes) {
        Pessoa pessoa = ps.open(id);

        if (pessoa == null) {
            redirectAttributes.addFlashAttribute("mensagem", "Não foi possivel localizar o registro!");
            return "redirect:/pessoa";
        }

        uiModel.addAttribute("pessoa", pessoa);
        return "pessoa/cadastro";
    }

    @RequestMapping(value = "{id}", params = "deletar", method = RequestMethod.GET)
    public String Deletar(@PathVariable Integer id, Model uiModel, RedirectAttributes redirectAttributes) {
        Pessoa pessoa = ps.open(id);

        if (pessoa == null) {
            redirectAttributes.addFlashAttribute("mensagem", "Não foi possivel localizar o registro!");
            return "redirect:/pessoa";
        }

        ps.delete(pessoa);
        redirectAttributes.addFlashAttribute("mensagem", "Pessoa excluida com sucesso!");

        return "redirect:/pessoa";
    }

}
