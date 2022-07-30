package com.diamones.springboot.app.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.diamones.springboot.app.models.dao.IClienteDao;
import com.diamones.springboot.app.models.entity.Cliente;


@Service
public class ClienteServiceImpl implements IClienteService {

	@Override
	@Transactional(readOnly = true)
	public List<Cliente> findAll() {

		return (List<Cliente>) clienteDao.findAll() ;
	}

	@Override
	@Transactional
	public void save(Cliente cliente) {

		clienteDao.save(cliente);

	}

	@Override
	@Transactional(readOnly = true)
	public Cliente findOne(Long id) {

		return clienteDao.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public void delete(Long id) {

		clienteDao.deleteById(id);

	}
	
	@Autowired
	private IClienteDao clienteDao;

}
