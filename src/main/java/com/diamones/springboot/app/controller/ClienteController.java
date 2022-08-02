package com.diamones.springboot.app.controller;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.diamones.springboot.app.model.service.IClienteService;
import com.diamones.springboot.app.models.entity.Cliente;

@Controller
@SessionAttributes("cliente")
public class ClienteController {
	
	@RequestMapping({"/", "","/index"})
	public String index(Model model) {
		
		model.addAttribute("titulo", "Primer CRUD");
		
		return "index";
	}
	
	@RequestMapping("/listar")
	public String listar(Model model) {
		
		model.addAttribute("titulo","Listado de clientes");
		model.addAttribute("clientes",clienteService.findAll());
		
		return "listar";
	}
	
	@RequestMapping("/form")
	public String crear(Map<String, Object> model) {
		
		Cliente cliente = new Cliente();
		
		model.put("cliente", cliente);
		model.put("titulo", "Creación de clientes");
		
		 return "form";
	}
	
	@RequestMapping(value="/form", method = RequestMethod.POST)
	public String guardar(@Valid Cliente cliente, BindingResult result, Model model, RedirectAttributes flash, SessionStatus status) {
		
		if (result.hasErrors()) {
			
			model.addAttribute("titulo", "Formulario cliente");
			
			return "form";			
		}
		
		String mensajeFlash = (cliente.getId()!=null)? "Cliente editado con éxito" : "Cliente creado con éxito";
		
		clienteService.save(cliente);
		status.setComplete();
		flash.addAttribute("success", mensajeFlash);
		
		return "redirect:listar";
	}
	
	@RequestMapping(value="/form{id}")
	public String editar(@PathVariable(value="id") Long id, Map<String,Object> model, RedirectAttributes flash) {
		
		Cliente cliente = null;
		
		if(id>0) {
			
			cliente = clienteService.findOne(id);
			
			if (cliente==null) {
				
				flash.addAttribute("error", "El ID del cliente no existe en la base de datos");
				
				return "redirect:listar";
			}
		}else {
			
			flash.addAttribute("error", "El ID del cliente no puede ser cero");
			
			return "redirect:listar";
		}
		
		model.put("cliente", cliente);
		model.put("titulo","Editar cliente");
		
		 return "form";
	}

	@RequestMapping(value="/eliminar{id}")
	public String eliminar(@PathVariable(value="id") Long id, RedirectAttributes flash) {
		
		if(id>0) {
			
			clienteService.delete(id);
			flash.addAttribute("success","Cliente eliminado con éxito");
		}
		
		return "redirect:listar";
	}
	
	@Autowired
	private IClienteService clienteService;

}
