package com.diamones.springboot.app.controller;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.diamones.springboot.app.model.service.IClienteService;
import com.diamones.springboot.app.models.entity.Cliente;
import com.diamones.springboot.app.util.paginator.PageRender;

@Controller
@SessionAttributes("cliente")
public class ClienteController {
	
	@RequestMapping({"/", "","/index"})
	public String index(Model model) {
		
		model.addAttribute("titulo", "Primer CRUD");
		
		return "index";
	}
	
	@RequestMapping("/ver/{id}")
	public String ver(@PathVariable(value="id") Long id, Map<String, Object> model, RedirectAttributes flash) {
		
		Cliente cliente = clienteService.findOne(id);
		
		if(cliente == null) {
			
			flash.addAttribute("error","El cliente no existe en la base de datos");
			
			return "redirect:listar";
		}
		
		model.put("cliente", cliente);
		model.put("titulo", "Detalle de cliente: " + cliente.getNombre().concat(" ").concat(cliente.getApellido()));

		return "ver";
	}
	
	@RequestMapping("/listar")
	public String listar(@RequestParam(name="page", defaultValue = "0") int page, Model model) {
		
		Pageable pageRequest = PageRequest.of(page, 10);
		
		Page<Cliente> clientes = clienteService.findAll(pageRequest);
		
		PageRender<Cliente> pageRender = new PageRender<>("/listar", clientes);
		
		model.addAttribute("titulo","Listado de clientes");
		model.addAttribute("clientes", clientes);
		model.addAttribute("page", pageRender);
		
		return "listar";
	}
	
	@RequestMapping("/form")
	public String crear(Map<String, Object> model) {
		
		Cliente cliente = new Cliente();
		
		model.put("cliente", cliente);
		model.put("titulo", "Creaci??n de clientes");
		
		 return "form";
	}
	
	@RequestMapping(value="/form", method = RequestMethod.POST)
	public String guardar(@Valid Cliente cliente, BindingResult result, Model model, RedirectAttributes flash, SessionStatus status) {
		
		if (result.hasErrors()) {
			
			model.addAttribute("titulo", "Formulario cliente");
			
			return "form";			
		}
		
		String mensajeFlash = (cliente.getId()!=null)? "Cliente editado con ??xito" : "Cliente creado con ??xito";
		
		clienteService.save(cliente);
		status.setComplete();
		flash.addFlashAttribute("success", mensajeFlash);
		
		
		return "redirect:listar";
	}
	
	@RequestMapping(value="/form/{id}")
	public String editar(@PathVariable(value="id") Long id, Map<String,Object> model, RedirectAttributes flash) {
		
		Cliente cliente = null;
		
		if(id>0) {
			
			cliente = clienteService.findOne(id);
			
			if (cliente==null) {
				
				flash.addFlashAttribute("error", "El ID del cliente no existe en la base de datos");
				
				return "redirect:listar";
			}
		}else {
			
			flash.addFlashAttribute("error", "El ID del cliente no puede ser cero");
			
			return "redirect:listar";
		}
		
		model.put("cliente", cliente);
		model.put("titulo","Editar cliente");
		
		 return "form";
	}

	@RequestMapping(value="/eliminar/{id}")
	public String eliminar(@PathVariable(value="id") Long id, RedirectAttributes flash) {
		
		if(id>0) {
			
			clienteService.delete(id);
			flash.addFlashAttribute("success","Cliente eliminado con ??xito");
		}
		
		return "redirect:/listar";
	}
	
	@Autowired
	private IClienteService clienteService;

}
