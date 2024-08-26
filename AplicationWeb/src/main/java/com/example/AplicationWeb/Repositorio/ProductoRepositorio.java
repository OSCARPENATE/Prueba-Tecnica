package com.example.AplicationWeb.Repositorio;

import com.example.AplicationWeb.Servicio.ProductoServicio;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductoRepositorio extends MongoRepository<ProductoServicio, String> {
}
